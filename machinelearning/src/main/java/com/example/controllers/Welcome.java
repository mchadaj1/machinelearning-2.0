package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Kontroler serwujący aplikację użytkownikowi.
 * Created by mateusz on 04.03.16.
 */
@Controller
public class Welcome {
    @RequestMapping("/")
    public ModelAndView index(ModelAndView mav)
    {
        mav.setViewName("index");
        return mav;
    }
}
