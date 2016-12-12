package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuniBlog.entity.Article;
import softuniBlog.entity.Picture;

import java.util.Set;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    Set<Picture> findByArticle(Article article);
}
