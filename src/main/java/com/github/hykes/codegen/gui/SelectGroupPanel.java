package com.github.hykes.codegen.gui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.constants.Defaults;
import com.github.hykes.codegen.gui.cmt.MyDialogWrapper;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.utils.GuiUtil;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/1/17
 */
public class SelectGroupPanel {
    private JPanel rootPanel;
    private JComboBox<CodeRoot> groupComboBox;
    private JPanel groupsPanel;

    private Project project;
    private final Map<String, String> groupPathMap = new HashMap<>();

    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * 获取group的输出路径
     * key: groupId
     * value: output path
     */
    public Map<String, String> getGroupPathMap() {
        List<String> selectGroups = GuiUtil.getAllJCheckBoxValue(groupsPanel);
        return groupPathMap.entrySet().stream()
                .filter(kv -> selectGroups.contains(kv.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 是否有选中
     */
    public Boolean hasSelected() {
        return !GuiUtil.getAllJCheckBoxValue(groupsPanel).isEmpty();
    }

    public SelectGroupPanel(List<CodeRoot> roots, Project project) {
        super();
        this.project = project;
        $$$setupUI$$$();
        for (CodeRoot root : roots) {
            groupComboBox.addItem(root);
        }
        groupComboBox.addActionListener(e -> {
            JComboBox source = (JComboBox) e.getSource();
            CodeRoot root = (CodeRoot) source.getSelectedItem();
            renderGroupsPanel(root);
        });
        renderGroupsPanel(roots.get(0));
    }

    private void renderGroupsPanel(CodeRoot root) {
        if (root == null) {
            return;
        }
        groupsPanel.removeAll();
        root.getGroups().forEach(it -> {
            JCheckBox groupBox = new JCheckBox(it.getName());
            groupBox.setName(it.getId());
            String path = SettingManager.getInstance().getRunningData(it.getId());
            //如果未选中，并且之前有数据则执行
            groupBox.addActionListener(box -> {
                if (groupBox.isSelected()) {
                    // 选择输出路径
                    SelectPathDialog dialog = new SelectPathDialog(project);
                    dialog.setAlwaysOnTop(true);
                    dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    // 包装对话框
                    MyDialogWrapper dialogWrapper = new MyDialogWrapper(project, dialog.getRootPane());
                    Toolkit kit = Toolkit.getDefaultToolkit();
                    Dimension screenSize = kit.getScreenSize();
                    dialogWrapper.setSize(350, 160);
                    dialogWrapper.setLocation((screenSize.width - dialog.getWidth()) / 2, (screenSize.height - dialog.getHeight()) / 2);
                    dialogWrapper.setResizable(false);
                    dialogWrapper.setActionOperator(dialog);
                    // dialog.setVisible(true);
                    dialogWrapper.show();
                    // 获取对应的值
                    String outputPath = dialog.getOutPutPath();
                    String basePackage = dialog.getBasePackage();
                    if (StringUtils.isEmpty(outputPath)) {
                        groupBox.setSelected(false);
                    } else {
                        groupPathMap.put(groupBox.getName(), outputPath + basePackage.replace(".", "/"));
                        SettingManager.getInstance().setRunningData(it.getId(), outputPath + basePackage.replace(".", "/"));
                    }
                }
            });
            if (path != null && path.length() > 2 && !groupBox.isSelected()) {
                groupBox.setSelected(true);
                groupPathMap.put(groupBox.getName(), path);
            }
            groupsPanel.add(groupBox);
        });
        groupsPanel.revalidate();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SelectGroupPanel");
        frame.setContentPane(new SelectGroupPanel(Defaults.getDefaultTemplates(), null).rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        groupComboBox = new ComboBox<>();
        groupComboBox.setRenderer(new CellRenderer());
        groupsPanel = new JPanel();
        groupsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 6.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(groupsPanel, gbc);
        groupComboBox.setToolTipText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(groupComboBox, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    public class CellRenderer extends JLabel implements ListCellRenderer {
        private static final long serialVersionUID = 4769047597333393201L;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            CodeRoot root = (CodeRoot) value;
            setHorizontalAlignment(JLabel.CENTER);
            try {
                this.setText(root.getName());
            } catch (Exception e) {
                this.setText(String.valueOf(index));
            }
            return this;
        }
    }
}
