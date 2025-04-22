package views;
import java.net.MalformedURLException;
import model.Product;
import model.ProdukModel;
import service.ProductService;
import model.PerishableProduct;
import model.NonPerishableProduct;
import model.DigitalProduct;
import model.BundleProduct;

import javax.swing.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.LogService;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

public class Produk extends javax.swing.JFrame {
    DefaultTableModel model;
    public static String setID;
    public static String setJenis;
    private ProdukPaket produkPaketFrame = null;
    /**
     * Creates new form Produk
     */
    public Produk() {
        initComponents();
        URL iconURL = getClass().getResource("/images/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());
        btnEnabled(false);
        refresh();
        reset();
    }

    private static void btnEnabled(boolean x){
        bUbah.setEnabled(x);
        bHapus.setEnabled(x);
    }
    
    private static void txtEnabled(boolean x){
        nama.setEnabled(x);
        harga.setEnabled(x);
        jenis.setEnabled(x);
    }
    
    private void cmbJenis() {
        Object selected = jenis.getSelectedItem();
        if (selected == null) return; // amanin kalau null

        String jenisSelected = selected.toString();
        if (jenisSelected.equals("Tidak Kedaluwarsa")) {
            lblKedaluwarsa.setVisible(false);
            lblURL.setVisible(false);
            lblVendor.setVisible(false);
            kedaluwarsa.setVisible(false);
            url.setVisible(false);
            vendor.setVisible(false);
        } else if (jenisSelected.equals("Kedaluwarsa")) {
            lblKedaluwarsa.setVisible(true);
            lblURL.setVisible(false);
            lblVendor.setVisible(false);
            kedaluwarsa.setVisible(true);
            url.setVisible(false);
            vendor.setVisible(false);
        } else if (jenisSelected.equals("Digital")) {
            lblKedaluwarsa.setVisible(false);
            lblURL.setVisible(true);
            lblVendor.setVisible(true);
            kedaluwarsa.setVisible(false);
            url.setVisible(true);
            vendor.setVisible(true);
        }
    }


    private void getDataTable() {
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // clear data
        List<Product> daftarProduk = ProductService.getAllProducts();

        for (Product p : daftarProduk) {
            String kedaluwarsa = "-";
            String vendor = "-";
            String diskon = "-";
            String produkPaket = "-";

            if (p instanceof PerishableProduct) {
                kedaluwarsa = ((PerishableProduct) p).getTanggalKedaluwarsa().toString();
            }

            if (p instanceof DigitalProduct) {
                vendor = ((DigitalProduct) p).getNamaVendor() + " / " + ((DigitalProduct) p).getUrl();
            }
            
            if (p instanceof BundleProduct) {
                diskon = String.valueOf(((BundleProduct) p).getDiskon());
                BundleProduct bp = (BundleProduct) p;
                StringBuilder isi = new StringBuilder();
                for (Product isiPaket : bp.getIsiPaket()) {
                    isi.append(isiPaket.getNama()).append(", ");
                }
                produkPaket = isi.toString();
            }

            model.addRow(new Object[] {
                    p.getId(),
                    p.getNama(),
                    p.getHarga(),
                    p.getJenis(),
                    kedaluwarsa,
                    vendor,
                    diskon,
                    produkPaket
            });
        }
    }
    
    private void refresh() {
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        getDataTable();
    }
    
    public static void reset() {
        jenis.removeAllItems();
        jenis.addItem("Tidak Kedaluwarsa");
        jenis.addItem("Kedaluwarsa");
        jenis.addItem("Digital");
        id.setText("");
        nama.setText("");
        harga.setText("");
        urlProduk.setText("");
        vendor.setText("");
        lblKedaluwarsa.setVisible(false);
        lblURL.setVisible(false);
        lblVendor.setVisible(false);
        kedaluwarsa.setVisible(false);
        url.setVisible(false);
        vendor.setVisible(false);
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
        setJenis = (table.getModel().getValueAt(row, 3).toString());
        btnEnabled(true);
    }
    
    private void insert() {
        try {
            String ID = id.getText();
            String Nama = nama.getText();
            double Harga = Double.parseDouble(harga.getText());
            Date Tanggal = kedaluwarsa.getDate();
            String Jenis = jenis.getSelectedItem().toString();

            if(Jenis.equals("Digital")) {
                String urlStr = urlProduk.getText().trim();
                String Vendor = vendor.getText().trim();

                if (urlStr.isEmpty() || Vendor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "URL dan Vendor tidak boleh kosong!");
                    return;
                }

                URL Url = new URL(urlStr); 
                DigitalProduct digital = new DigitalProduct(ID, Nama, Harga, Url, Vendor);
                ProductService.simpanProdukDigital(digital);
            } else if(Jenis.equals("Kedaluwarsa")) {
                if (Tanggal == null){
                    JOptionPane.showMessageDialog(this, "Isi tanggal terlebih dahulu.");
                    return;
                }
                PerishableProduct perishable = new PerishableProduct(ID, Nama, Harga, Tanggal);
                ProductService.simpanProdukKedaluwarsa(perishable);
            } else {
                NonPerishableProduct nonPerishable = new NonPerishableProduct(ID, Nama, Harga);
                ProductService.simpanProdukTidakKedaluwarsa(nonPerishable);
            }

            refresh();
            reset();
            JOptionPane.showMessageDialog(this, "Data Produk berhasil ditambahkan", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);

        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(this, "URL tidak valid.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Data Produk gagal ditambahkan!");
            ex.printStackTrace(); // buat debug
        }
    }
    
    private void update() {
        String tanggal;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(kedaluwarsa.getDate() != null){
            tanggal = dateFormat.format(kedaluwarsa.getDate());
        } else{
            tanggal = null;
        }
        String ID = id.getText();
        String Nama = nama.getText();
        double Harga = Double.parseDouble(harga.getText());
        String Jenis = jenis.getSelectedItem().toString();
        String Url = urlProduk.getText();
        String Vendor = vendor.getText();
        try{
            if(Jenis.equals("Digital")){
                try {
                        URL url = new URL(urlProduk.getText());
                    } catch (MalformedURLException ex) {
                        JOptionPane.showMessageDialog(this, "URL tidak valid.");
                        return;
                    }
            }
            ProductService.updateProduk(ID, Nama, Harga, Jenis, tanggal, Url, Vendor);
            LogService.tambahLog("Ubah Produk", "Mengubah Produk dengan ID :"+ID+" menjadi => Nama :"+Nama+", Harga:"+Harga+", Jenis:"+Jenis+", Tgl Kedaluwarsa:"+tanggal+", Url:"+Url+", Vendor:"+Vendor);
            refresh();
            reset();
            JOptionPane.showMessageDialog(this,
                "Data Produk berhasil diubah",
                "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Data Produk gagal diubah!");
        }
    }
    
    private void delete() {
        ProductService.deleteProduk(setID);
    }
    
    private void hasilCari() throws MalformedURLException {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String keyword = cari.getText().trim();
        List<Product> hasil = ProductService.searchProduk(keyword);

        model.setRowCount(0); // clear tabel dulu

        for (Product p : hasil) {
            String kedaluwarsa = "-";
            String vendor = "-";
            String diskon = "-";
            String produkPaket = "-";

            if (p instanceof PerishableProduct) {
                kedaluwarsa = ((PerishableProduct) p).getTanggalKedaluwarsa().toString();
            }

            if (p instanceof DigitalProduct) {
                vendor = ((DigitalProduct) p).getNamaVendor() + " / " + ((DigitalProduct) p).getUrl();
            }

            if (p instanceof BundleProduct) {
                diskon = String.valueOf(((BundleProduct) p).getDiskon());
                BundleProduct bp = (BundleProduct) p;
                StringBuilder isi = new StringBuilder();
                for (Product isiPaket : bp.getIsiPaket()) {
                    isi.append(isiPaket.getNama()).append(", ");
                }
                produkPaket = isi.toString();
            }

            model.addRow(new Object[] {
                    p.getId(),
                    p.getNama(),
                    p.getHarga(),
                    p.getJenis(),
                    kedaluwarsa,
                    vendor,
                    diskon,
                    produkPaket
            });
        }
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
        harga = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lblKedaluwarsa = new javax.swing.JLabel();
        lblURL = new javax.swing.JLabel();
        lblVendor = new javax.swing.JLabel();
        jenis = new javax.swing.JComboBox<>();
        kedaluwarsa = new com.toedter.calendar.JDateChooser();
        vendor = new javax.swing.JTextField();
        url = new javax.swing.JScrollPane();
        urlProduk = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        bRefresh = new javax.swing.JButton();
        bTambah = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bTambahPaket = new javax.swing.JButton();
        cari = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Produk - POS");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Form Produk");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 16, -1, -1));

        jLabel3.setText("ID Produk");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 54, -1, -1));

        id.setEnabled(false);
        jPanel2.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 51, 80, -1));
        jPanel2.add(nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 85, 189, -1));

        jLabel4.setText("Nama Produk");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 88, -1, -1));

        bBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cencel.png"))); // NOI18N
        bBatal.setText("Batal");
        bBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBatalActionPerformed(evt);
            }
        });
        jPanel2.add(bBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, -1, -1));

        bSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        bSimpan.setText("Simpan");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });
        jPanel2.add(bSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, -1, -1));

        jLabel6.setText("Harga");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 122, -1, -1));
        jPanel2.add(harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 119, 189, -1));

        jLabel7.setText("Jenis Produk");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 156, -1, -1));

        lblKedaluwarsa.setText("Tgl Kedaluwarsa");
        jPanel2.add(lblKedaluwarsa, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 193, 99, -1));

        lblURL.setText("URL");
        jPanel2.add(lblURL, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 221, -1, -1));

        lblVendor.setText("Nama Vendor");
        jPanel2.add(lblVendor, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 286, -1, -1));

        jenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jenis.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jenisItemStateChanged(evt);
            }
        });
        jPanel2.add(jenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 153, 139, -1));
        jPanel2.add(kedaluwarsa, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 187, 100, -1));
        jPanel2.add(vendor, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 283, 189, -1));

        urlProduk.setBorder(null);
        urlProduk.setDragEnabled(true);
        url.setViewportView(urlProduk);

        jPanel2.add(url, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 221, 189, 50));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Produk", "Nama", "Harga", "Jenis", "Kedaluwarsa", "URL/Vendor", "Diskon paket", "Isi paket"
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

        bTambahPaket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png"))); // NOI18N
        bTambahPaket.setText("Tambah Paket");
        bTambahPaket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTambahPaketActionPerformed(evt);
            }
        });

        cari.setBorder(javax.swing.BorderFactory.createTitledBorder("Cari data"));
        cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bTambahPaket)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bHapus)
                        .addGap(12, 12, 12)
                        .addComponent(bRefresh)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bTambah)
                    .addComponent(bHapus)
                    .addComponent(bUbah)
                    .addComponent(bRefresh)
                    .addComponent(bTambahPaket)
                    .addComponent(cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setBackground(new java.awt.Color(0, 153, 255));
        jLabel1.setFont(new java.awt.Font("Californian FB", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Data Produk");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    }// </editor-fold>//GEN-END:initComponents

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_bBatalActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        if(bSimpan.getText().equals("Simpan")){
            if(!id.getText().trim().isEmpty() &&
                !nama.getText().trim().isEmpty() &&
                !harga.getText().trim().isEmpty() ) {
                insert();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Lengkapi from terlebih dahulu!",
                    "Notifikasi", JOptionPane.WARNING_MESSAGE);
            }
        }else{
            if(!id.getText().trim().isEmpty() &&
                !nama.getText().trim().isEmpty() &&
                !harga.getText().trim().isEmpty() ) {
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

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_bRefreshActionPerformed

    private void bTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTambahActionPerformed
        // TODO add your handling code here:
        if (produkPaketFrame != null && produkPaketFrame.isDisplayable()) {
            produkPaketFrame.dispose();
        }
        refresh();
        reset();
        String newId = ProductService.idOtomatis();
        id.setText(newId);
        txtEnabled(true);
        bSimpan.setEnabled(true);
        bBatal.setEnabled(true);
    }//GEN-LAST:event_bTambahActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        bSimpan.setText("Simpan Perubahan");
        ProdukModel produk = ProductService.getProduk(setID);
        if (produk != null) {
            if(setJenis.equals("Paket")){
               ProdukPaket pp = new ProdukPaket();
                pp.setVisible(true);
                pp.bSimpan.setText("Simpan Perubahan");
                pp.lblJudul.setText("Simpan Produk Paket");
                pp.idProdukPaket.setText(produk.getId());
                pp.namaPaket.setText(produk.getNama());
                pp.hargaPaket.setText(String.valueOf(produk.getHarga()));

                List<Product> isiPaket = ProductService.getIsiPaket(setID);

                DefaultTableModel model = (DefaultTableModel) pp.table.getModel();
                model.setRowCount(0);

                for (Product item : isiPaket) {
                    model.addRow(new Object[]{
                        item.getId(),
                        item.getNama(),
                        item.getHarga()
                    });
                }
            } else{
            id.setText(produk.getId());
            nama.setText(produk.getNama());
            harga.setText(String.valueOf(produk.getHarga()));
            jenis.setSelectedItem(produk.getJenisProduk());

            if ("Kedaluwarsa".equals(produk.getJenisProduk()) && produk.getTanggalKedaluwarsa() != null) {
                java.util.Date sqlDate = produk.getTanggalKedaluwarsa();
                kedaluwarsa.setDate(sqlDate);
            }

            if ("Digital".equals(produk.getJenisProduk())) {
                urlProduk.setText(produk.getUrl());
                vendor.setText(produk.getVendor());
            }
            bSimpan.setEnabled(true);
            bBatal.setEnabled(true);
            txtEnabled(true);
            btnEnabled(false);
            }
        }
    }//GEN-LAST:event_bUbahActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        int alert = JOptionPane.showConfirmDialog(this,
            "Anda yakin ingin menghapus produk " + setID,
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

    private void bTambahPaketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTambahPaketActionPerformed
        // TODO add your handling code here:
         if (produkPaketFrame == null || !produkPaketFrame.isDisplayable()) {
            produkPaketFrame = new ProdukPaket();
            produkPaketFrame.setVisible(true);
        } else {
            produkPaketFrame.toFront(); // Bawa ke depan jika sudah ada
            produkPaketFrame.requestFocus();
        }
    }//GEN-LAST:event_bTambahPaketActionPerformed

    private void jenisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jenisItemStateChanged
        // TODO add your handling code here:
        cmbJenis();
    }//GEN-LAST:event_jenisItemStateChanged

    private void cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariKeyReleased
        try {
            hasilCari();
            btnEnabled(false);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Produk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cariKeyReleased

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
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Produk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton bBatal;
    public static javax.swing.JButton bHapus;
    private transient javax.swing.JButton bRefresh;
    public static javax.swing.JButton bSimpan;
    private javax.swing.JButton bTambah;
    private javax.swing.JButton bTambahPaket;
    public static javax.swing.JButton bUbah;
    private javax.swing.JTextField cari;
    public static javax.swing.JTextField harga;
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
    public static javax.swing.JComboBox<String> jenis;
    public static com.toedter.calendar.JDateChooser kedaluwarsa;
    public static javax.swing.JLabel lblKedaluwarsa;
    public static javax.swing.JLabel lblURL;
    public static javax.swing.JLabel lblVendor;
    public static javax.swing.JTextField nama;
    private javax.swing.JTable table;
    public static javax.swing.JScrollPane url;
    public static javax.swing.JTextPane urlProduk;
    public static javax.swing.JTextField vendor;
    // End of variables declaration//GEN-END:variables
}
