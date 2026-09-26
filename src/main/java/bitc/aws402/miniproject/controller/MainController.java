package bitc.aws402.miniproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    // http://localhost:8080/ 및 http://localhost:8080/index 둘 다 접속 가능
    @GetMapping({"", "/", "/index"})
    public String index() {
        return "index"; // templates/index.html 파일을 리턴
    }

//    alert 창 출력을 위한 메소드
    private String alert(String msg, String url, Model model){
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "common/alert";
    }
}