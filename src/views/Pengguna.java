/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;
import model.PenggunaModel;
import service.PenggunaService;

import javax.swing.*;
import java.net.URL;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import static service.PenggunaService.validasiUsername;
import static service.PenggunaService.validasiUsernameUpdate;

public class Pengguna extends javax.swing.JFrame {
    DefaultTableModel model;
    public static String setID;
    public static String setNama;
    public static String setUser;
    /**
     * Creates new form Pengguna
     */
    public Pengguna() {
        initComponents();
        URL iconURL = getClass().getResource("/images/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());
        btnEnabled(false);
        refresh();
        reset();
    }
    
    private void btnEnabled(boolean x){
        bUbah.setEnabled(x);
        bHapus.setEnabled(x);
    }
    
    private void txtEnabled(boolean x){
        nama.setEnabled(x);
        username.setEnabled(x);
        password.setEnabled(x);
    }

    private void getDataTable() {
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<PenggunaModel> penggunaList = PenggunaService.getAllPengguna();
        for (PenggunaModel pengguna : penggunaList) {
            model.addRow(new Object[]{
                pengguna.getId(),
                pengguna.getUsername(),
                pengguna.getName()
            });
        }

        // Set model ke JTable
        table.setModel(model);
    }
    
    private void refresh() {
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        getDataTable();
    }
    
    private void reset() {
        id.setText("");
        nama.setText("");
        username.setText("");
        password.setText("");
        nama.requestFocus();
        bSimpan.setEnabled(false);
        bBatal.setEnabled(false);
        bSimpan.setText("Simpan");
        btnEnabled(false);
        txtEnabled(false);
    }
    
    private void getFromTable(){
        int row = table.getSelectedRow();
        setID = (table.getModel().getValueAt(row, 0).toString());
        setNama = (table.getModel().getValueAt(row, 1).toString());
        setUser = (table.getModel().getValueAt(row, 2).toString());
        btnEnabled(true);
    }
    
    private void insert() {
        String ID = id.getText();
        String Nama = nama.getText();
        String Username = username.getText();
        String Password = password.getText();
        if (validasiUsername(Username)) {
            JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
            return;
        }else{
            PenggunaModel penggunaP = new PenggunaModel(ID,  Username, Password, Nama);
                boolean success = PenggunaService.addPengguna(penggunaP);

                if (success) {
                    refresh();
                    reset();
                    JOptionPane.showMessageDialog(this,
                         "Data Pengguna berhasil ditambahkan",
                         "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan data pengguna!", "Error", JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    
    private void update() {
        String ID = id.getText();
        String Nama = nama.getText();
        String Username = username.getText();
        String Password = password.getText();
        if(Password.isEmpty()){
            if (validasiUsernameUpdate(username.getText(), id.getText())) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
                return;
            }else{
                PenggunaModel pengguna = new PenggunaModel(ID, Username, Nama);
                boolean success = PenggunaService.updatePengguna(pengguna);

                if (success) {
                    refresh();
                        reset();
                        JOptionPane.showMessageDialog(this,
                             "Data Pengguna berhasil diubah",
                             "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah data pengguna!", "Error", JOptionPane.ERROR_MESSAGE);
                }       
            }
        }else{
            if (validasiUsernameUpdate(username.getText(), id.getText())) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
                return;
            }else{
                PenggunaModel penggunaP = new PenggunaModel(ID,  Username, Password, Nama);
                boolean success = PenggunaService.updatePenggunaP(penggunaP);

                if (success) {
                    refresh();
                    reset();
                    JOptionPane.showMessageDialog(this,
                         "Data Pengguna berhasil diubah",
                         "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah data pengguna!", "Error", JOptionPane.ERROR_MESSAGE);
                }       
            }
        }
    }
    
    private void delete() {
        PenggunaService.deletePengguna(setID);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        nama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        bBatal = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        password = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        bRefresh = new javax.swing.JButton();
        bTambah = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Pengguna - POS");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Form Pengguna");

        jLabel3.setText("ID Pengguna");

        id.setEnabled(false);

        jLabel4.setText("Nama Pengguna");

        bBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cencel.png"))); // NOI18N
        bBatal.setText("Batal");
        bBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBatalActionPerformed(evt);
            }
        });

        bSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        bSimpan.setText("Simpan");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

        jLabel6.setText("Username");

        jLabel7.setText("Password");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bBatal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSimpan))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(password)
                            .addComponent(username)
                            .addComponent(nama)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 96, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bBatal)
                    .addComponent(bSimpan))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Pengguna", "Nama Pengguna", "Username"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        bRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reload.png"))); // NOI18N
        bRefresh.setText("Refresh");
        bRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRefreshActionPerformed(evt);
            }
        });

        bTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png"))); // NOI18N
        bTambah.setText("Tambah");
        bTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTambahActionPerformed(evt);
            }
        });

        bUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        bUbah.setText("Ubah");
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });

        bHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        bHapus.setText("Hapus");
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bHapus)
                        .addGap(10, 10, 10)
                        .addComponent(bRefresh)
                        .addGap(0, 77, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bTambah)
                    .addComponent(bHapus)
                    .addComponent(bUbah)
                    .addComponent(bRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setBackground(new java.awt.Color(0, 153, 255));
        jLabel1.setFont(new java.awt.Font("Californian FB", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Data Pengguna");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_bRefreshActionPerformed

    private void bTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTambahActionPerformed
        // TODO add your handling code here:
        refresh();
        reset();
        String newId = PenggunaService.IdOtomatis();
        id.setText(newId);
        txtEnabled(true);
        bSimpan.setEnabled(true);
        bBatal.setEnabled(true);
    }//GEN-LAST:event_bTambahActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        bSimpan.setText("Simpan Perubahan");
        id.setText(setID);
        nama.setText(setNama);
        username.setText(setUser);
        bSimpan.setEnabled(true);
        bBatal.setEnabled(true);
        txtEnabled(true);
        btnEnabled(false);
    }//GEN-LAST:event_bUbahActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        int alert = JOptionPane.showConfirmDialog(this,
                    "Anda yakin ingin menghapus pengguna " + setNama,
                    "Notifikasi", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if(alert == JOptionPane.YES_OPTION) {
               delete();
               refresh();
               reset();
               JOptionPane.showMessageDialog(this,
                    "Data Pengguna berhasil dihapus",
                    "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                refresh();
            }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_bBatalActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        if(bSimpan.getText().equals("Simpan")){
            if(!id.getText().trim().isEmpty() &&
               !nama.getText().trim().isEmpty() &&
               !username.getText().trim().isEmpty() &&
               !password.getText().trim().isEmpty() ) {
                insert();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Lengkapi from terlebih dahulu!",
                    "Notifikasi", JOptionPane.WARNING_MESSAGE);
            }
        }else{
            if(!id.getText().trim().isEmpty() &&
               !nama.getText().trim().isEmpty() &&
               !username.getText().trim().isEmpty() ) {
               update();
             } else {
               JOptionPane.showMessageDialog(this,
                    "Lengkapi from terlebih dahulu!",
                    "Notifikasi", JOptionPane.WARNING_MESSAGE);
             }
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
        getFromTable();
    }//GEN-LAST:event_tableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pengguna().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBatal;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bRefresh;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bTambah;
    private javax.swing.JButton bUbah;
    public static javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField password;
    private javax.swing.JTable table;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
