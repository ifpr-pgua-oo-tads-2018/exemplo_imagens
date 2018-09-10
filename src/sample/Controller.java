package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private ImageView imgView;

    @FXML
    private Button btEsq,btDir;


    private Imagem img;
    private ArrayList<Imagem> lista;
    private int pos=0;

   @FXML
   public void initialize(){
       btEsq.setDisable(true);
       btDir.setDisable(true);
   }

    @FXML
    private void abrir(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        File f = fileChooser.showOpenDialog(null);
        if(f !=null){
            Image im = new Image(f.toURI().toString(),100,150,true,false);
            img = new Imagem();
            img.setImage(im);
            img.setNomeArquivo("imgs/"+f.getName()+".jpg");
            imgView.setImage(img.getImage());
        }
    }

    @FXML
    private void salvarDisco(){
        try{
            JDBCImageDAO.getInstance().salvarNoDisco(img);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarBanco(){
        try{
            JDBCImageDAO.getInstance().salvarDiretoNoBanco(img);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void carregarImgs(){
        try{
            lista = JDBCImageDAO.getInstance().lista();
            if(lista.size()>0) {
                imgView.setImage(lista.get(pos).getImage());

                btEsq.setDisable(false);
                btDir.setDisable(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void passaImagem(ActionEvent evt){
       if(evt.getSource() == btEsq){
           pos -=1;
       }else if(evt.getSource() == btDir){
           pos +=1;
       }

       if(pos < 0 ){
           pos=lista.size()-1;
       }
       if(pos >=lista.size()){
           pos=0;
       }

        imgView.setImage(lista.get(pos).getImage());

    }


}
