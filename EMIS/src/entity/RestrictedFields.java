package entity;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class RestrictedFields {
    private final String[] edu_str={"专科","本科","硕士","博士"};
    private final String[] mar_str={"yes","no"};
    private final String[] dep_str={"行政部","财务部","质量管理部","营运部","技术部","维修部","人力资源部"};
    private final String[][] tit_str={{
            "办公室主任","行政总监","行政经理","行政专员","行政部部长"
    },{
            "统计员","税务经理","税务专员","财务部部长"
    },{
            "质量检验员","认证工程师","供应商","质量管理部部长"
    },{
            "销售总监","销售经理","区域销售经理","营运部部长"
    },{
            "技术部经理","组长","组员","技术部部长"
    },{
            "维修部主管","工程维修专员","设备维修专员","物业维修专员","维修部部长"
    },{
            "人力资源经理","人力资源专员","招聘主管","员工培训与发展主管","人力资源部部长"
    }
    };

    private JSpinner edu_spi;
    private JSpinner mar_spi;
    private JSpinner dep_spi;
    private JSpinner tit_spi;

    public void setTitle(){
        Object department=dep_spi.getValue();
        for(int i=0;i<dep_str.length;i++){
            if(department.toString().equals(dep_str[i])){
                tit_spi.setModel(new SpinnerListModel(tit_str[i]));
            }
        }
    }

    public void initRestrictedFields(Font font,JFrame jf){
        //学历spinner
        edu_spi=new JSpinner();
        edu_spi.setFont(font);
        edu_spi.setBounds(275,180,150,30);
        jf.add(edu_spi);

        //婚姻状况spinner
        mar_spi=new JSpinner();
        mar_spi.setFont(font);
        mar_spi.setBounds(275,230,150,30);
        jf.add(mar_spi);


        //职称状况spinner
        tit_spi=new JSpinner();
        tit_spi.setFont(font);
        tit_spi.setBounds(275,330,150,30);
        jf.add(tit_spi);

        //部门spinner
        dep_spi=new JSpinner();
        dep_spi.setFont(font);
        dep_spi.setBounds(275,280,150,30);
        dep_spi.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setTitle();
            }
        });
        jf.add(dep_spi);
    }

    public void initRestrictedFields(Font font,JPanel panel){
        //学历spinner
        edu_spi=new JSpinner();
        edu_spi.setFont(font);
        edu_spi.setBounds(172,0,86,32);
        panel.add(edu_spi);

        //婚姻状况spinner
        mar_spi=new JSpinner();
        mar_spi.setFont(font);
        mar_spi.setBounds(258,0,86,32);
        panel.add(mar_spi);

        //职称状况spinner
        tit_spi=new JSpinner();
        tit_spi.setFont(new Font("宋体",Font.PLAIN,12));
        tit_spi.setBounds(344,0,86,32);
        panel.add(tit_spi);

        //部门spinner
        dep_spi=new JSpinner();
        dep_spi.setFont(font);
        dep_spi.setBounds(430,0,86,32);
        dep_spi.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setTitle();
            }
        });
        panel.add(dep_spi);
    }

    public void initInfo(){
        edu_spi.setModel(new SpinnerListModel(edu_str));
        mar_spi.setModel(new SpinnerListModel(mar_str));
        dep_spi.setModel(new SpinnerListModel(dep_str));
        tit_spi.setModel(new SpinnerListModel(tit_str[0]));
    }

    public String getEdu_level(){
        return edu_spi.getValue().toString();
    }
    public String getMar_status(){
        return mar_spi.getValue().toString();
    }
    public String getJob_title(){
        return tit_spi.getValue().toString();
    }
    public String getDep_name(){
        return dep_spi.getValue().toString();
    }

}
