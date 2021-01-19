package start.services.contracts;

import start.entities.Post;
import start.entities.User;
import start.entities.models.PostBindingModel;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    Post getPostById(Integer id);

    Post addPost(PostBindingModel post, User user, String photoUrl,String status);

    List<Post> getAllPublicFriendsPosts(List<User> friends,User loginUser);

    List<Post> getAllPostByUser(User user);

    void deletePostById(Integer id);

    List<Post> getPersonalPosts();

    Post save(Post post);

}
