package com.github.hykes.codegen.gui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.PackageChooser;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class SelectPathDialog extends JDialog implements ActionOperator {
    private JPanel contentPane;
    private JTextField outPutText;
    private JButton selectOutputBtn;
    private JTextField packageText;
    private JButton selectPackageBtn;
    private String outputPath;
    private String basePackage;

    public SelectPathDialog(Project project) {
        setContentPane(contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        selectOutputBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                descriptor.setTitle("Select output path");
                descriptor.setShowFileSystemRoots(false);
                descriptor.setDescription("Select output path");
                descriptor.setHideIgnored(true);
                descriptor.setRoots(project.getBaseDir());
                descriptor.setForcedToUseIdeaFileChooser(true);
                VirtualFile virtualFile = FileChooser.chooseFile(descriptor, project, project.getBaseDir());
                if (Objects.nonNull(virtualFile)) {
                    String output = virtualFile.getPath();
                    PsiDirectory psiDirectory = PsiDirectoryFactory.getInstance(project).createDirectory(virtualFile);
                    PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(psiDirectory);
                    if (psiPackage != null && psiPackage.getName() != null) {
                        final StringBuilder path = new StringBuilder();
                        path.append(psiPackage.getName());
                        while (psiPackage.getParentPackage() != null && psiPackage.getParentPackage().getName() != null) {
                            psiPackage = psiPackage.getParentPackage();
                            if (path.length() > 0) {
                                path.insert(0, '.');
                            }
                            path.insert(0, psiPackage.getName());
                        }
                        packageText.setText(path.toString());
                        output = output.replace(path.toString().replace(".", "/"), "");
                    }
                    outPutText.setText(StringUtils.applyPath(output));
                }
            }
        });
        selectPackageBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                PackageChooser packageChooser = new PackageChooserDialog("Select Base Package", project);
                packageChooser.getWindow().setAlwaysOnTop(true);
                packageChooser.show();
                switch (packageChooser.getExitCode()) {
                    case PackageChooser.OK_EXIT_CODE:
                        PsiPackage psiPackage = packageChooser.getSelectedPackage();
                        if (Objects.nonNull(psiPackage)) {
                            packageText.setText(psiPackage.getQualifiedName());
                        }
                        break;
                }
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String getOutPutPath() {
        return outputPath;
    }

    public String getBasePackage() {
        return basePackage;
    }

    @Override
    public void ok() {
        outputPath = outPutText.getText();
        basePackage = packageText.getText();
        System.out.println("outputPath==========="+outputPath);
        System.out.println("basePackage==========="+basePackage);
        setVisible(false);
    }

    @Override
    public void cancel() { }

    @Override
    public boolean valid() { return true; }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Output Path");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        outPutText = new JTextField();
        panel1.add(outPutText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectOutputBtn = new JButton();
        selectOutputBtn.setText("...");
        panel1.add(selectOutputBtn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Base Package");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        packageText = new JTextField();
        panel1.add(packageText, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectPackageBtn = new JButton();
        selectPackageBtn.setText("...");
        panel1.add(selectPackageBtn, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
