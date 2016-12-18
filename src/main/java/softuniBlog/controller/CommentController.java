package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.CommentBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Comment;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CommentRepository;
import softuniBlog.repository.UserRepository;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/article/{id}/add-comment")
    @PreAuthorize("isAuthenticated()")
    public String submitComment(@PathVariable Integer id, CommentBindingModel commentBindingModel) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }
        Article article = this.articleRepository.findOne(id);

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User author = this.userRepository.findByEmail(user.getUsername());

        Comment comment = new Comment(commentBindingModel.getContent(), article, author);

        this.commentRepository.saveAndFlush(comment);

        return "redirect:/article/" + id;
    }

    @GetMapping("/comment/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){

        if (!this.commentRepository.exists(id)){
            return "redirect:/";
        }

        Comment comment=this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)){
            return "redirect:/article/"+comment.getArticle().getId();
        }
        model.addAttribute("comment",comment);
        model.addAttribute("view","comment/edit");

        return "base-layout";

    }

    @PostMapping("/comment/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, CommentBindingModel commentBindingModel){

        if (!this.commentRepository.exists(id)){
            return "redirect:/";
        }

        Comment comment=this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)){
            return "redirect:/article/"+comment.getArticle().getId();
        }

        comment.setContent(commentBindingModel.getContent());
        this.commentRepository.saveAndFlush(comment);

        return "redirect:/article/"+comment.getArticle().getId();
    }

    @GetMapping("/comment/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable Integer id, Model model){
        if (!this.commentRepository.exists(id)){
            return "redirect:/";
        }
        Comment comment=this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)){

            return "redirect:/article/"+comment.getArticle().getId();
        }
        model.addAttribute("comment",comment);
        model.addAttribute("view","comment/delete");

        return "base-layout";
    }

    @PostMapping("/comment/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){
        if (!this.commentRepository.exists(id)){
            return "redirect:/";
        }
        Comment comment=this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)){

            return "redirect:/article/"+comment.getArticle().getId();
        }

        this.commentRepository.delete(id);

        return "return:/article/"+comment.getArticle().getId();
    }

    private boolean isUserAuthorOrAdmin(Comment comment){

        UserDetails user=(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity=this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin()||userEntity.isAuthor(comment);
    }

}
