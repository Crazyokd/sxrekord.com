package frame;

import DBConnectionManager.DBConnectionManager;
import entity.Employee;
import entity.Photo;
import entity.RestrictedFields;
import entity.User;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.border.LineBorder;

public class RUDFrame {

    public JFrame frame;
    private JTable table;
    private Font font=new Font("黑体",Font.PLAIN,20);

    private JTextField id_tf;
    private JTextField name_tf;

    private JTextField manager_id_tf;
    private JTextField salary_tf;

    private RestrictedFields rf=new RestrictedFields();


    private Object[][] objects;
    private String[] columnNames=new String[] {
        "ID", "\u59D3\u540D", "\u5B66\u5386", "\u5A5A\u59FB\u72B6\u51B5", "\u804C\u79F0", "\u90E8\u95E8\u540D", "老板ID", "月薪资", "\u7167\u7247"
    };
    private int pre_row=-1;
    private boolean if_del;
//    private Photo photo=new Photo();
    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//            }
//        });
//    }

    /**
     * Create the application.
     */
    public RUDFrame() {
        getData();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("员工信息管理系统-查改删");
        frame.setBounds(100, 100, 1100, 598);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        initScrollPane();

        initSelectPanel();


        JButton btnNewButton = new JButton("查询信息");
        btnNewButton.setBounds(434, 442, 126, 32);
        frame.getContentPane().add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Employee ue=new Employee((int)StrConvertNumber(id_tf.getText()),name_tf.getText(), rf.getEdu_level(),
                            rf.getMar_status(), rf.getJob_title(), rf.getDep_name(),
                            (int)StrConvertNumber(manager_id_tf.getText()),StrConvertNumber(salary_tf.getText()));
                    queryExactly(ue);
                    refreshTable();
                }catch (NumberFormatException nfe){
                    System.out.println("有字段不符合规范");
                }


            }

        });

        JButton btnNewButton_1 = new JButton("全表查询");
        btnNewButton_1.setBounds(268, 445, 113, 27);
        frame.getContentPane().add(btnNewButton_1);

        btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getData();
                refreshTable();
            }
        });

        JButton btnNewButton_2 = new JButton("删除信息");
        btnNewButton_2.setBounds(613, 445, 113, 27);
        frame.getContentPane().add(btnNewButton_2);

        btnNewButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(DBConnectionManager.getCur_user().getType().equals("admin"))
                deleteInfo();
            }
        });

        //修改照片
        JButton photo_btn=new JButton("选择照片");
        photo_btn.setFont(font);
        photo_btn.setBounds(925,150,120,30);
        photo_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser photo_fc=Photo.choosePhoto();
                if(photo_fc.showOpenDialog(frame)==0){
                    int cur_row=table.getSelectedRow();


                    Photo photo=new Photo();
                    photo.setPhoto_path(photo_fc.getSelectedFile().getAbsolutePath());

                    table.setValueAt(photo,cur_row,8);


//                    objects[cur_row][8]=new Photo(objects[cur_row][0].toString()+objects[cur_row][1]);
                    pre_row=cur_row;
                }

            }
        });
        frame.getContentPane().add(photo_btn);

        JButton check_btn=new JButton("查看照片");
        check_btn.setFont(font);
        check_btn.setBounds(925,200,120,30);
        check_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cur_row=table.getSelectedRow();
                if(cur_row>-1){
                    Photo photo=((Photo)table.getValueAt(cur_row,8));
                    if(photo.getPhoto_path()!=null){
                        photo.viewPhoto();
                    }else{
                        System.out.println("暂时还没有该用户的照片信息呢");
                    }
                }else{
                    System.out.println("您还未选择任何图片哦");
                }


            }
        });
        frame.getContentPane().add(check_btn);
    }

    public double StrConvertNumber(String str) throws NumberFormatException{
        double rst;
        if(str.equals("")){
            rst=-1.0;
        }else{
            rst=Double.parseDouble(str);
        }
        return rst;
    }

    public void initTable(){
        table = new JTable(){
            //设置是否可编辑
            @Override
            public boolean isCellEditable(int row,int column){
                //封掉五列
                if(DBConnectionManager.getCur_user().getType().equals("admin")){
                    if(column>3&&column<7||column==0||column==8)return false;
                    else return true;
                }
                else{
                    return false;
                }
            }
        };
//        table.setBorder(new LineBorder(new Color(0, 0, 0), 3));
        table.setModel(new DefaultTableModel(objects,columnNames) {
            Class[] columnTypes = new Class[] {
                    Integer.class, String.class, String.class, String.class, String.class, String.class, Integer.class, Double.class, Object.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        table.setRowHeight(32);

        centerText();

        table.getModel().addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e) {
//                if(e.getType() == TableModelEvent.UPDATE){
                saveModify(e);
            }
        });
    }

    /**
     * 设置表格文字居中显示
     */
    public void centerText(){
        DefaultTableCellRenderer r=new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0;i<9;i++){
            table.getColumn(table.getColumnName(i)).setCellRenderer(r);
        }
    }

    public void initScrollPane(){
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(100, 13, 794, 375);
        frame.getContentPane().add(scrollPane);

        initTable();
        scrollPane.setViewportView(table);
    }

    public void initSelectPanel(){
        JLabel lblNewLabel = new JLabel("查询选项:");
        lblNewLabel.setFont(new Font("黑体", Font.BOLD, 18));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(0, 397, 100, 32);
        frame.getContentPane().add(lblNewLabel);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.GREEN));
        panel.setBackground(Color.ORANGE);
        panel.setBounds(100, 397, 688, 32);
        panel.setLayout(null);

        frame.getContentPane().add(panel);

        //文本区
        id_tf=new JTextField();
        id_tf.setBounds(0,0,86,32);
        panel.add(id_tf);
        id_tf.setColumns(10);

        name_tf=new JTextField();
        name_tf.setBounds(86,0,86,32);
        panel.add(name_tf);
        name_tf.setColumns(10);

        //初始化约束字段
        rf.initRestrictedFields(font,panel);

        manager_id_tf=new JTextField();
        manager_id_tf.setBounds(516,0,86,32);
        panel.add(manager_id_tf);
        manager_id_tf.setColumns(10);

        salary_tf=new JTextField();
        salary_tf.setBounds(602,0,86,32);
        panel.add(salary_tf);
        salary_tf.setColumns(10);


        initInfo();
    }

    public void initInfo(){
        id_tf.setText("");
        name_tf.setText("");
        salary_tf.setText("");
        rf.initInfo();
    }

    public void getData(){
        String sql_sel="select * from view_all_info";
        try{
            //初始化表格数据
            DBConnectionManager.getPS(sql_sel);
            objects=listToArray(DBConnectionManager.getTableList());
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void queryExactly(Employee e){
        try {
            if (e.getEmployee_id() != -1) {
                //存在id
                queryFromID(e.getEmployee_id());
            } else {
                //多字段查询
                queryFromMultiField(e);
            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public void updateInfoToDB(){
        Employee e=new Employee((Integer)table.getValueAt(pre_row,0),(String)table.getValueAt(pre_row,1),
                (String)table.getValueAt(pre_row,2),(String)table.getValueAt(pre_row,3),
                (Double)table.getValueAt(pre_row,7),(Photo) table.getValueAt(pre_row,8));
        if(checkInfo(e)){
            if(DBConnectionManager.modifyInfoToDB(e)>0){
                //更新table
                updateTable(e);
            }
        }
        else{
            //还原信息
            restoreTable();
        }
    }

    public void restoreTable(){
        table.setValueAt(objects[pre_row][1],pre_row,1);
        table.setValueAt(objects[pre_row][2],pre_row,2);
        table.setValueAt(objects[pre_row][3],pre_row,3);
        table.setValueAt(objects[pre_row][7],pre_row,7);
        table.setValueAt(objects[pre_row][8],pre_row,8);
    }

    public void updateTable(Employee e){
        objects[pre_row][1]=e.getName();
        table.setValueAt(e.getName(),pre_row,1);
        objects[pre_row][2]=e.getEdu_level();
        table.setValueAt(e.getEdu_level(),pre_row,2);
        objects[pre_row][3]=e.getMarital_status();
        table.setValueAt(e.getMarital_status(),pre_row,3);
        objects[pre_row][7]=e.getSalary();
        table.setValueAt(e.getSalary(),pre_row,7);
        //复制图片，保存图片
        copyFile(e.getPhoto().getPhoto_path(),
        objects[pre_row][0].toString()+objects[pre_row][1]);
        objects[pre_row][8]=new Photo(objects[pre_row][0].toString()+objects[pre_row][1]);
    }

    /**
     * 检查员工学历、婚姻状况、薪资是否符合要求
     * @param e,待检查的员工
     * @return
     */
    public boolean checkInfo(Employee e){
        boolean res=false;
        String message;
        if(check(e.getEdu_level(),new String[]{
                "专科","本科","硕士","博士"
        })){
            if(check(e.getMarital_status(),new String[]{
                    "yes","no"
            })){
                if(DBConnectionManager.checkSalary(e.getEmployee_id(),e.getSalary())){
                    res=true;
                    message="修改成功";
                }else{
                    message="薪资不符合要求";
                }
            }else{
                message="婚姻状况填写错误,可选值(yes,no)";
            }
        }else{
            message="学历填写错误,可选值(专科,本科,硕士,博士)";
        }
        JOptionPane.showMessageDialog(frame,message);
        return res;
    }

    /**
     * 辅助checkInfo进行字段检查
     * @param str
     * @param strs
     * @return
     */
    public boolean check(String str,String[] strs){
        for(int i=0;i<strs.length;i++){
            if(str.equals(strs[i])){
                return true;
            }
        }
        return false;
    }


    public void deleteInfo(){
        int selectedRow=table.getSelectedRow();
        if(selectedRow>-1){
            int id=(Integer)table.getValueAt(selectedRow,0);
            int if_delete=JOptionPane.showConfirmDialog(frame,"确定删除ID为"+id+"所在行？",
                    "删除信息",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(if_delete==JOptionPane.OK_OPTION){
                if (DBConnectionManager.deleteInfoFromDB(id)) {
                    if_del=true;
                    ((DefaultTableModel)table.getModel()).removeRow(selectedRow);
                    if_del=false;
                    //将员工对应的图片缓存也一同删除
                    File file=new File(((Photo)objects[selectedRow][8]).getPhoto_path());
                    if(file.exists()){
                        file.delete();
                    }
                }else{
                    JOptionPane.showMessageDialog(frame,"无法删除部长");
                }
            }
        }else{
            System.out.println("亲，您还没有选中任何行哦");
        }
    }

    public void queryFromID(int id) throws SQLException{
        String sql="select *\n" +
                "from view_all_info\n" +
                "where employee_id=?";
        DBConnectionManager.getPS(sql,id);
        objects=listToArray(DBConnectionManager.getTableList());
    }

    public void queryFromMultiField(Employee e) throws SQLException{
        String sql="select *\n" +
                "from view_all_info\n" +
                "where edu_level=? and\n" +
                "marital_status=? and\n" +
                "job_title=? and\n" +
                "department_name=?";
        //不确定查询
        DBConnectionManager.getPS(sql,e);
        ArrayList<Object[]> alo=DBConnectionManager.getTableList();

        for (int i = 0; i < alo.size(); i++) {
            String o_name = alo.get(i)[1].toString();
            String o_ma_id = alo.get(i)[6].toString();
            String o_sal = alo.get(i)[7].toString();
            String name = e.getName();
            String manager_id =((Integer)e.getManager_id()).toString();
            String salary=((Double)e.getSalary()).toString();
            if (!((name.equals("")||name.equals(o_name))&&
                    manager_id.equals("-1")||manager_id.equals(o_ma_id)&&
                    salary.equals("-1.0")||salary.equals(o_sal))){
                alo.remove(i);
            }
        }
        objects=listToArray(alo);
    }

    /**
     * 将ArrayList转换为需要的二维数组
     * @param als,待转换的ArrayList
     * @return
     */
    public Object[][] listToArray(ArrayList<Object[]> als){
        Object[][] tmpObjects=new Object[als.size()][9];
        for(int i=0;i<als.size();i++){
            for(int j=0;j<als.get(i).length;j++){
                if(als.get(i)[j]!=null){
                    tmpObjects[i][j]=als.get(i)[j];
                }
                else {
                    tmpObjects[i][j] = "暂无";
                }
            }
        }
        return tmpObjects;
    }

    public void refreshTable(){
        //刷新数据
        if_del=true;
        ((DefaultTableModel)table.getModel()).setDataVector(objects,columnNames);
        centerText();
        ((DefaultTableModel)table.getModel()).fireTableDataChanged();
        if_del=false;
    }

    /**
     * 复制文件从filePath到Photo.path_prefix+newFileName+Photo.path_suffix
     * @param filePath,被复制的文件路径
     * @param newFileName,复制后的新文件名
     */
    public void copyFile(String filePath,String newFileName){
        FileInputStream fis=null;
        FileOutputStream fos=null;
        try {
            fis=new FileInputStream(new File(filePath));
            fos=new FileOutputStream(new File(Photo.path_prefix+newFileName+Photo.path_suffix));

            byte[] b=new byte[1024];
            int len;
            while((len=fis.read(b))!=-1){
                fos.write(b,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveModify(TableModelEvent e){
        if(!if_del){
            int tmp_row=e.getLastRow();

            //当前选中行与上一次更改数据的行不同，提示是否保存修改
            if(pre_row!=tmp_row&&pre_row!=-1){

                int if_update=JOptionPane.showConfirmDialog(frame,"是否保存上一轮更新的数据？",
                        "修改信息",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(if_update==JOptionPane.OK_OPTION){
                    updateInfoToDB();
                    System.out.println("修改信息");
                }else{
                    restoreTable();
                    System.out.println("就是玩！");
                }
                pre_row=-1;

            }

            if(!table.getValueAt(tmp_row,e.getColumn()).equals(objects[tmp_row][e.getColumn()])){
                pre_row=tmp_row;
            }
        }
    }
    public static void main(String args[]){
        DBConnectionManager.setCur_user(new User("admin","123","admin"));
        new RUDFrame().frame.setVisible(true);
    }
}
