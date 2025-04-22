/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

public class LogModel {
    private int idLog;
    private LocalDateTime waktu;
    private String aksi;
    private String keterangan;

    public LogModel(String aksi, String keterangan) {
        this.aksi = aksi;
        this.keterangan = keterangan;
    }

    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }

    public String getAksi() { return aksi; }
    public void setAksi(String aksi) { this.aksi = aksi; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
}

