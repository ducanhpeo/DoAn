package com.example.datnsum24sd01.dto;

import com.example.datnsum24sd01.enumation.TrangThai;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ChiTietSanPhamDTO {
    private Long id;
    private String anhChinh;
    private String tenSanPham;
    private String tenThuongHieu;
    private String tenDongSanPham;
    private String tenMauSac;
    private String tenKichThuoc;
    private Integer soLuongTon;
    private BigDecimal giaMacDinh;
    private BigDecimal giaBan;
    //    private KhuyenMai khuyenMai;
    private TrangThai tenTrangThai;
    private LocalDate ngayTao;
    private Long idSanPham;
}
