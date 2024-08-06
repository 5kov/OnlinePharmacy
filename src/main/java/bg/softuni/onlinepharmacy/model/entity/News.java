package bg.softuni.onlinepharmacy.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String titleEn;
    @Column(unique = true, nullable = false)
    private String titleBg;
    @Column(nullable = false, columnDefinition="TEXT")
    private String contentEn;
    @Column(nullable = false, columnDefinition="TEXT")
    private String contentBg;

    public News() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleBg() {
        return titleBg;
    }

    public void setTitleBg(String titleBg) {
        this.titleBg = titleBg;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getContentBg() {
        return contentBg;
    }

    public void setContentBg(String contentBg) {
        this.contentBg = contentBg;
    }
}
