package com.lunz.springit.controller;

import com.lunz.springit.config.AuditorAwareImpl;
import com.lunz.springit.domain.Comment;
import com.lunz.springit.domain.Link;
import com.lunz.springit.domain.User;
import com.lunz.springit.service.BeanUtil;
import com.lunz.springit.service.CommentService;
import com.lunz.springit.service.LinkService;
import com.lunz.springit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);
    private LinkService linkService;
    private CommentService commentService;
    private UserService userService;

    public LinkController(LinkService linkService, CommentService commentService, UserService userService) {
        this.linkService = linkService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("links",linkService.findAll());
        return "link/list";
    }

    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id, Model model) {
        Optional<Link> link = linkService.findById(id);
        if (link.isPresent()) {
            Link currentLink = link.get();
            Comment comment = new Comment();
            comment.setLink(currentLink);
            model.addAttribute("comment",comment);
            model.addAttribute("link",currentLink);
            model.addAttribute("success",model.containsAttribute("success"));
            return "link/view";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link",new Link());
        return "link/submit";
    }

    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model,
                             RedirectAttributes redirectAttributes) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (bindingResult.hasErrors()) {
            logger.info("Validation errors were found while submitting a new link.");
            model.addAttribute("link",link);
            return "link/submit";
        } else {
            //save our link
            link.setUser(user);
            linkService.save(link);
            logger.info("New link was saved successfully");
            redirectAttributes
                    .addAttribute("id",link.getId())
                    .addFlashAttribute("success",true);
            //return "redirect:/";
            return "redirect:/link/{id}";
        }
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/link/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (bindingResult.hasErrors()){
            logger.info("There was a problem adding a new comment.");
        } else {
            comment.setUser(user);
            commentService.save(comment);
            logger.info("New comment was saved successfully.");
        }
        return "redirect:/link/" + comment.getLink().getId();
    }

}