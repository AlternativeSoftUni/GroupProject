package softuniBlog.bindingModel;


import org.springframework.web.multipart.MultipartFile;

public class PictureBindingModel {

    private MultipartFile picture;

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
