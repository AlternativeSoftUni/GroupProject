package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import softuniBlog.entity.Article;
import softuniBlog.entity.Picture;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.PictureRepository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Controller
public class PictureController {

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private ArticleRepository articleRepository;


    @RequestMapping(value = "/article/{id}/picture", method = RequestMethod.GET)
    public void showImage(HttpServletResponse response, @PathVariable Integer id) throws Exception {

         Article article = articleRepository.getOne(id);
         Set<Picture> picture = pictureRepository.findByArticle(article);

        byte[] imgByte = picture.iterator().next().getPicture();

        response.setContentType("image/jpeg");

        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(imgByte);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
