package entity;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Photo {
    public static final String LOOK_UP="点击查看";
    public static final String WITHOUT="暂无图片";
    public static final String path_prefix="C:/Users/86182/Desktop/src/src/imgcache/";
    public static final String path_suffix=".jpg";
    private String photo_path;
    private String photo_name;//不带后缀

    public Photo(){}

    public Photo(String photo_name){
        setPhoto_name(photo_name);
    }

    public String getPhoto_name(){
        return photo_name;
    }
    public String getPhoto_path(){
        return photo_path;
    }
    public void setPhoto_path(String photo_path){
        this.photo_path=photo_path;
    }
    public void setPhoto_name(String photo_name){
        this.photo_name=photo_name;
        this.photo_path=path_prefix+photo_name+path_suffix;
    }
    @Override
    public String toString(){
        String ans;
        if(photo_path==null){
            ans=WITHOUT;
        }else{
            ans=LOOK_UP;
        }
        return ans;
    }

    public static JFileChooser choosePhoto(){
        JFileChooser photo_fc=new JFileChooser("C:/Users/86182/Desktop");
        photo_fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                photo_fc.addChoosableFileFilter(
//                        new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
        photo_fc.setFileFilter(
                new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
        return photo_fc;
    }
    public void viewPhoto(){
        if(photo_path!=null){
            JFrame img_frame=new JFrame();

            img_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            ImageIcon icon = new ImageIcon(photo_path);
            JLabel label = new JLabel(icon);
//                System.out.println(icon.getIconWidth());
//                System.out.println(icon.getIconHeight());
            img_frame.setLocation(100,100);
            img_frame.add(label);
            img_frame.pack();

            img_frame.setVisible(true);
        }
    }

    /**
     * 将数据库中的照片字段写入文件
     * @param rs
     * @param fileName
     * @return
     * @throws SQLException
     */
    public Photo readPhoto(ResultSet rs, String fileName) throws SQLException {
        Blob b= rs.getBlob("photo");
        if(b!=null){
            this.setPhoto_name(fileName);
            OutputStream out = null ;
            try {
                out = new FileOutputStream(this.getPhoto_path()) ;
                out.write(b.getBytes(1,(int)b.length())) ;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(out!=null) {
                    try {
                        out.close() ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this;
    }
}
