package softuniBlog.entity;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture {

    private Integer id;
    private byte[] picture;
    private Article article;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Lob
    @Column(name = "pictures", nullable = false, columnDefinition = "longblob")
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @ManyToOne
    @JoinColumn(name = "article_id")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Picture() {
    }

    public Picture(byte[] picture, Article article) {
        this.picture = picture;
        this.article = article;
    }
}
