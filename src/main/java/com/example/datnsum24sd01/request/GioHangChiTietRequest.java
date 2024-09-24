package com.example.datnsum24sd01.request;

import com.example.datnsum24sd01.entity.ChiTietSanPham;
import com.example.datnsum24sd01.entity.GioHang;
import com.example.datnsum24sd01.entity.HoaDon;
import com.example.datnsum24sd01.enumation.TrangThai;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class GioHangChiTietRequest {
    private Long id;

    private ChiTietSanPham chiTietSanPham;

    private HoaDon hoaDon;

    private GioHang gioHang;

    private BigDecimal donGia;

    private Integer soLuong;

    private TrangThai trangThai;

    private String ghiChu;

}
