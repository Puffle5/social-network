package start.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import start.entities.Post;
import start.entities.Relationship;
import start.entities.User;
import start.entities.enumerators.PostStatus;
import start.entities.enumerators.RelationshipStatus;
import start.entities.models.PostBindingModel;
import start.exceptions.EntityNotFoundException;
import start.find.login.user.FindLoginUser;
import start.repositories.PostRepository;
import start.repositories.RelationshipRepository;
import start.repositories.UserRepository;
import start.services.contracts.PostService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.getAllByActiveTrue();

        if (CollectionUtils.isEmpty(posts)) {
            throw new EntityNotFoundException("There are no posts.");
        }

        return posts;
    }


    @Override
    public List<Post> getAllPublicFriendsPosts(List<User> friends, User loginUser) {
        List<Post> friendsPosts = new ArrayList<>();

        for (User user : friends) {
            if (user.getActive()) {
                friendsPosts.addAll(postRepository.getAllByUserAndActiveTrueAndStatusOrderByDateDesc(user, PostStatus.PUBLIC));
            }
        }

        friendsPosts.addAll(postRepository.getAllByUserAndActiveTrue(loginUser));

        if (CollectionUtils.isEmpty(friendsPosts)) {
            throw new EntityNotFoundException("There are no posts.");
        }

        return friendsPosts;
    }

    @Override
    public List<Post> getAllPostByUser(User user) {
        List<Post> postsByUser = postRepository.getAllByUserAndActiveTrueAndStatusOrderByDateDesc(user, PostStatus.PUBLIC);
        if (CollectionUtils.isEmpty(postsByUser)) {
            throw new EntityNotFoundException("There are no posts.");
        }
        return postsByUser;
    }

    @Override
    public Post getPostById(Integer id) {
        Post post = postRepository.getByIDAndActiveTrue(id);
        if (post == null) {
            throw new EntityNotFoundException("There is no post.");
        }
        return post;
    }

    @Override
    public void deletePostById(Integer id) {
        Post post = postRepository.getByIDAndActiveTrue(id);
        if (post == null) {
            throw new EntityNotFoundException("There is no post for deleting.");
        }

        post.setActive(false);
        postRepository.saveAndFlush(post);
    }

    @Override
    public List<Post> getPersonalPosts() {
        User loginUser = userRepository.getByUsernameAndActiveTrue(FindLoginUser.findLoginUserName());
        if (loginUser == null) {
            throw new EntityNotFoundException("There is no login user.");
        }
        List<Post> posts = postRepository.findAllByUserAndActiveTrueOrderByDateDesc(loginUser);

        if (CollectionUtils.isEmpty(posts)) {
            throw new EntityNotFoundException("There are no posts from login user.");
        }

        return posts;
    }


    @Override
    public Post addPost(PostBindingModel post, User user, String photoUrl, String status) {
        Post newPost = new Post(post.getID(), post.getTitle(), post.getDescription(), true);
        LocalDateTime currentDate = LocalDateTime.now();
        Instant instant = currentDate.atZone(ZoneId.systemDefault()).toInstant();
        newPost.setDate(Date.from(instant));
        newPost.setPhotoURL(photoUrl);
        newPost.setUser(user);
        newPost.setStatus(PostStatus.valueOf(status.toUpperCase()));

        return postRepository.saveAndFlush(newPost);
    }

    @Override
    public Post save(Post post) {
        return postRepository.saveAndFlush(post);
    }

}