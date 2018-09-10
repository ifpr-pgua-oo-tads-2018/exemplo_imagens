/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import jdk.nashorn.internal.scripts.JD;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author haperlin
 */
public class JDBCImageDAO {

    private static JDBCImageDAO instance;

    private JDBCImageDAO(){

    }

    public static JDBCImageDAO getInstance(){
        if(instance == null){
            instance = new JDBCImageDAO();
        }
        return instance;
    }

    
    public void salvarDiretoNoBanco(Imagem img) throws SQLException,IOException{
        Connection con = FabricaConexao.getConnection();
        
        PreparedStatement pStmt = con.prepareStatement("insert into imagens (imagem) values (?)");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bImg = SwingFXUtils.fromFXImage(img.getImage(),null);

        ImageIO.write(bImg, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        
        pStmt.setBinaryStream(1, is,is.available());
        pStmt.executeUpdate();
        
        pStmt.close();
        con.close();
    }
    
    public void salvarNoDisco(Imagem img) throws SQLException,IOException{
        Connection con = FabricaConexao.getConnection();
        
        PreparedStatement pStmt = con.prepareStatement("insert into imagens (nomeArquivo) values (?)");

        pStmt.setString(1, img.getNomeArquivo());
        pStmt.executeUpdate();
        
        pStmt.close();
        con.close();

        //salva no disco
        File f = new File(img.getNomeArquivo());
        FileOutputStream fos = new FileOutputStream(f);
        BufferedImage bImg = SwingFXUtils.fromFXImage(img.getImage(),null);
        ImageIO.write(bImg, "jpg", fos);

    }
    
    public ArrayList<Imagem> lista() throws SQLException,IOException{
        ArrayList lista = new ArrayList();
        Connection con = FabricaConexao.getConnection();
        
        PreparedStatement pStmt = con.prepareStatement("select * from imagens");
        
        ResultSet rs = pStmt.executeQuery();
        
        while(rs.next()){
            String nomeArquivo = rs.getString("nomeArquivo");
            BufferedImage bim;
            int id = rs.getInt("id");
            if(nomeArquivo == null){
                bim = ImageIO.read(rs.getBinaryStream("imagem"));
            }else{
                bim = ImageIO.read(new File(nomeArquivo));
            }
            Image im = SwingFXUtils.toFXImage(bim,null);
            Imagem img = new Imagem();
            img.setImage(im);
            img.setNomeArquivo(nomeArquivo);
            img.setId(id);
            lista.add(img);
        }
        rs.close();
        pStmt.close();
        con.close();
        return lista;
    }
       
}
