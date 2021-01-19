package start.controllers.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import start.services.contracts.CommentService;

@Controller
@RequestMapping()
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comment/delete/{id}")
    String deleteUserByName(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return "redirect:/admin";
    }
}
