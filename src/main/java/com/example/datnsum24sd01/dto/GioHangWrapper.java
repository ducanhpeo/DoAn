package com.example.datnsum24sd01.dto;

import com.example.datnsum24sd01.entity.GioHangChiTiet;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Data
public class GioHangWrapper {
    private List<GioHangChiTiet> listGioHangChiTiet;

    public List<GioHangChiTiet> getListGioHangChiTiet() {
        return listGioHangChiTiet;
    }

    public void setListGioHangChiTiet(List<GioHangChiTiet> listGioHangChiTiet) {
        this.listGioHangChiTiet = listGioHangChiTiet;
    }
}
