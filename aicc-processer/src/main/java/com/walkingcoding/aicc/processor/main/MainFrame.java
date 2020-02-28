package com.walkingcoding.aicc.processor.main;


import javax.swing.*;
import java.io.File;

/**
 * 主程序界面入口，选择参数
 *
 * @author songhuiqing
 */
public class MainFrame extends JFrame {

    private Boolean argsSuccess = false;
    private String workspace;
    private String templateFileFolder = "templates/default";
    private int parallelSize = 3;

    public MainFrame() {
        this.setTitle("课件国产化加工工具");
        this.setSize(600, 200);
        JPanel panel = new JPanel();
        // 添加面板
        this.add(panel);
        placeComponents(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    private void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 工作空间
        JTextField workspaceField = addFileSelectArea(panel, "工作空间", "选择工作空间", 20);
        // 课件模版
        JTextField templateField = addFileSelectArea(panel, "课件模版", "选择课件模版", 50);
        templateField.setText(this.templateFileFolder);
        // 并发数量文本域
        JLabel parallelSizeLabel = new JLabel("并发数量:");
        parallelSizeLabel.setBounds(10, 80, 80, 25);
        panel.add(parallelSizeLabel);
        // 并发数量输入框
        JTextField parallelSizeText = new JTextField(20);
        parallelSizeText.setText(String.valueOf(this.parallelSize));
        parallelSizeText.setBounds(100, 80, 165, 25);
        panel.add(parallelSizeText);

        // 开始
        JButton loginButton = new JButton("开始");
        loginButton.setBounds(100, 110, 80, 25);
        loginButton.addActionListener(e -> {
            // 参数
            this.workspace = workspaceField.getText();
            this.templateFileFolder = templateField.getText();
            this.parallelSize = Integer.parseInt(parallelSizeText.getText());
            this.argsSuccess = true;
            this.setVisible(false);
        });
        panel.add(loginButton);
    }

    private JTextField addFileSelectArea(JPanel panel, String label, String chooseTitle, int y) {
        JLabel templateLabel = new JLabel(label);

        templateLabel.setBounds(10, y, 80, 25);
        panel.add(templateLabel);

        JTextField templateText = new JTextField(20);
        templateText.setEnabled(false);
        templateText.setBounds(100, y, 400, 25);
        panel.add(templateText);

        JButton templateFileBtn = new JButton("选择");
        templateFileBtn.setBounds(510, y, 80, 25);
        panel.add(templateFileBtn);
        templateFileBtn.addActionListener(e -> {
            File file = chooseFile(chooseTitle);
            templateText.setText(file.getPath());
        });
        return templateText;
    }

    /**
     * 选择文件
     *
     * @return
     */
    private File chooseFile(String title) {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showDialog(new JLabel(), title);
        return jfc.getSelectedFile();
    }

    public String getWorkspace() {
        return workspace;
    }

    public String getTemplateFileFolder() {
        return templateFileFolder;
    }

    public int getParallelSize() {
        return parallelSize;
    }

    public Boolean getArgsSuccess() {
        return argsSuccess;
    }
}
