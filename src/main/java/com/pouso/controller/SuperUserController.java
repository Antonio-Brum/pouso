package com.pouso.controller;

import com.pouso.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuperUserController {

    private final UsuarioRepository usuarioRepository;

    public SuperUserController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/sudo")
    public String sudo(HttpSession session, Model model) {
        String cpf = (String) session.getAttribute("cpf");
        if (cpf == null) {
            return "redirect:/login";
        }

        String nivel = usuarioRepository.buscarNivelAdmin(cpf);
        if (!"S".equals(nivel)) {
            return "redirect:/home";
        }

        model.addAttribute("usuarios", usuarioRepository.listarTodos());
        return "superUser";
    }
}
