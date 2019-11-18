package com.example.prixa.model;

public class NodeTree {
    private int id;
    private String namaNode;
    private int Parrent;
    private String value;
    private Boolean is_data;
    private int spasi;

    public NodeTree(int id, String namaNode, int parrent, String value, Boolean is_data,int spasi) {
        this.id = id;
        this.namaNode = namaNode;
        Parrent = parrent;
        this.value = value;
        this.is_data = is_data;
        this.spasi = spasi;
    }

    public NodeTree() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaNode() {
        return namaNode;
    }

    public void setNamaNode(String namaNode) {
        this.namaNode = namaNode;
    }

    public int getParrent() {
        return Parrent;
    }

    public void setParrent(int parrent) {
        Parrent = parrent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getIs_data() {
        return is_data;
    }

    public void setIs_data(Boolean is_data) {
        this.is_data = is_data;
    }

    public int getSpasi() {
        return spasi;
    }

    public void setSpasi(int spasi) {
        this.spasi = spasi;
    }
}
