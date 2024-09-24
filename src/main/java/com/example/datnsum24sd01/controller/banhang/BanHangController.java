package com.example.datnsum24sd01.controller.banhang;



import com.example.datnsum24sd01.configure.VNPayConfig;
import com.example.datnsum24sd01.dto.VNPayResponse;
import com.example.datnsum24sd01.entity.HoaDon;
import com.example.datnsum24sd01.entity.HoaDonChiTiet;
import com.example.datnsum24sd01.entity.SanPham;
import com.example.datnsum24sd01.exportHD.HoaDonPDF;
import com.example.datnsum24sd01.service.BanHangService;
import com.example.datnsum24sd01.service.ChiTietSanPhamService;
import com.example.datnsum24sd01.service.KhachHangService;
import com.example.datnsum24sd01.service.PhieuGiamGiaService;
import com.example.datnsum24sd01.service.impl.PaymentServiceImpl;
import com.example.datnsum24sd01.utils.VNPayUtil;
import com.example.datnsum24sd01.worker.Spingsecurity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/ban-hang")
public class BanHangController {


    @Autowired
    private BanHangService banHangService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @Autowired
    private PhieuGiamGiaService maGiamGiaService;

    @Autowired
    private  VNPayUtil vnPayUtil;

    private Boolean isActive = false;

    private Boolean checkHoaDon = false;
    private Spingsecurity spingsecurity = new Spingsecurity();


    @Value("${payment.vnPay.employeeReturnUrl}")
    private String vnpReturnUrl;

    @Autowired
    private PaymentServiceImpl paymentService;

    @GetMapping
    public String hienThiBanHang(Model model) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        model.addAttribute("listHoaDonCho", banHangService.getHoaDonCho());
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("hoaDonCho", new HoaDon());
        model.addAttribute("hoaDonChiTiet", new HoaDonChiTiet());
        model.addAttribute("checkHoaDon", checkHoaDon);
        model.addAttribute("checkBtn", true);
        model.addAttribute("thanhTien", new BigDecimal(Double.valueOf(0)));
        model.addAttribute("xu", new BigDecimal(Double.valueOf(0)));
        model.addAttribute("tenNhanVien", spingsecurity.getCurrentNhanVienTen());
        return "admin-template/ban_hang/ban_hang";
    }

    @GetMapping("/login")
    public String login(Model model) {

        return "admin-template/login";
    }


    @GetMapping("/thongtinnhanvien")
    public String test(Model model) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        model.addAttribute("tenNhanVien", spingsecurity.getCurrentNhanVienTen());
        model.addAttribute("id", spingsecurity.getCurrentNhanVienId());

        return "admin-template/thongtinnhanvien";
    }
    @GetMapping("/hoa-don/{idHoaDon}")
    public String hienThiHoaDonChiTiet(@PathVariable("idHoaDon") String idHoaDon,
                                       Model model) {

        isActive = true;
        BigDecimal tongTien = banHangService.getTongTien(Long.valueOf(idHoaDon));
        HoaDon hoaDon = banHangService.getOneById(Long.valueOf(idHoaDon));
        model.addAttribute("listHoaDonChiTiet", banHangService.getHoaDonChiTietByIdHoaDon(Long.valueOf(idHoaDon)));
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("thanhTien", banHangService.getThanhTien(Long.valueOf(idHoaDon), tongTien));
        model.addAttribute("listHoaDonCho", banHangService.getHoaDonCho());
        model.addAttribute("listSanPham", chiTietSanPhamService.getAll());
        model.addAttribute("hoaDonCho", banHangService.getOneById(Long.valueOf(idHoaDon)));
        model.addAttribute("listKhachHang", khachHangService.getList());
        model.addAttribute("listMaGiamGia", maGiamGiaService.getListHoatDong());
        model.addAttribute("idHoaDonCho", idHoaDon);
        model.addAttribute("hoaDonChiTiet", new HoaDonChiTiet());
        model.addAttribute("isActive", isActive);
        model.addAttribute("checkHoaDon", checkHoaDon == true);
        model.addAttribute("checkBtn", false);
        model.addAttribute("xu", hoaDon.getKhachHang().getTichDiem());
        model.addAttribute("giamGia", banHangService.voucher(Long.valueOf(idHoaDon), tongTien));
        model.addAttribute("tenNhanVien", spingsecurity.getCurrentNhanVienTen());
        return "admin-template/ban_hang/ban_hang";
    }

    @PostMapping("/tao-hoa-don")
    public String taoHoaDon(@ModelAttribute("hoaDon") HoaDon hoaDon,
                            Model model) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }

        HoaDon hoaDon1 = banHangService.themHoaDon(hoaDon, idNhanVien);
        if (hoaDon1 == null){
            model.addAttribute("warning", "Vượt quá số lượng hóa đơn chờ!");
            return "redirect:/admin/ban-hang?warning";
        }
        model.addAttribute("success", "Thêm thành công");
        return "redirect:/admin/ban-hang?success";
    }

    @PostMapping("/hoa-don/{idHoaDon}/them-khach-hang/{idKhachHang}")
    public String updateKhachHang(@PathVariable("idHoaDon") String idHoaDon,
                                  @PathVariable("idKhachHang") String idKhachHang) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        banHangService.updateKhachHang(Long.valueOf(idHoaDon), Long.valueOf(idKhachHang));
        return "redirect:/admin/ban-hang/hoa-don/{idHoaDon}";
    }


    @PostMapping("/hoa-don/{idHoaDon}/them-san-pham/{idSanPham}")
    public String themHoaDonChitiet(@PathVariable("idHoaDon") String idHoaDonCho,
                                    @PathVariable("idSanPham") String idSanPham,
                                    @ModelAttribute("hoaDonChiTiet") HoaDonChiTiet hoaDonChiTiet,Model model) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null) {
            return "redirect:/login";
        }
        banHangService.taoHoaDonChiTiet(Long.valueOf(idSanPham), Long.valueOf(idHoaDonCho), hoaDonChiTiet);
        banHangService.updateSoLuong(Long.valueOf(idSanPham), hoaDonChiTiet.getSoLuong());
        return "redirect:/admin/ban-hang/hoa-don/{idHoaDon}";
    }

    @GetMapping("/hoa-don/{idHoaDon}/xoa-hoa-don-chi-tiet/{idHoaDonChiTiet}")
    public String xoaHoaDonChitiet(@PathVariable("idHoaDon") String idHoaDon,
                                   @PathVariable("idHoaDonChiTiet") String idHoaDonChiTiet) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        banHangService.updateSoLuongTuHDCT(Long.valueOf(idHoaDonChiTiet));
        banHangService.xoaHoaDonChiTiet(Long.valueOf(idHoaDonChiTiet));
        BigDecimal tongTien = banHangService.getTongTien(Long.valueOf(idHoaDon));
        banHangService.checkGiamGia(Long.valueOf(idHoaDon), tongTien);
        return "redirect:/admin/ban-hang/hoa-don/" + idHoaDon;
    }

    @PostMapping("/hoa-don/xuat-hoan-don/{idHoaDonCho}")
    public String xuatHoaDon(@PathVariable("idHoaDonCho") String idHoaDon,
                             @RequestParam("tongTien") String tongTien,
                             @RequestParam("thanhTien") String thanhTien,
                             @RequestParam(value = "xuTichDiem", defaultValue = "false") Boolean xuTichDiem) throws Exception {
//        Long idNhanVien = principalKhachHang.getCurrentNhanVienId();
//        if (idNhanVien == null){
//            return "redirect:/login";
//        }
        banHangService.checkXuHoaDon(Long.valueOf(idHoaDon), tongTien, thanhTien, xuTichDiem);
        BigDecimal tienGiamGia = banHangService.voucher(Long.valueOf(idHoaDon), BigDecimal.valueOf(Double.valueOf(tongTien)));
        banHangService.thanhToanHoaDon(Long.valueOf(idHoaDon), tienGiamGia);
        HoaDon hoaDon = banHangService.getOneById(Long.valueOf(idHoaDon));
        //Xuat hoa don
        List<HoaDonChiTiet> listHDCT = banHangService.getHoaDonChiTietByIdHoaDon(Long.valueOf(idHoaDon));
        BigDecimal giamGia = banHangService.voucher(Long.valueOf(idHoaDon), BigDecimal.valueOf(Double.valueOf(tongTien)));
        HoaDonPDF hoaDonPDF = new HoaDonPDF();
        hoaDonPDF.exportToPDF(listHDCT, hoaDon, giamGia);
        banHangService.updateGiamGia(Long.valueOf(idHoaDon));
        banHangService.tichDiem(hoaDon.getKhachHang().getId(), thanhTien);
        return "redirect:/admin/ban-hang";
    }

//    @PostMapping("/thanh-toan/{idHoaDonCho}")
//    public String thanhToanHoaDon( Model model,@PathVariable("idHoaDonCho") String idHoaDon,
//                                  @RequestParam("tongTien") String tongTien,
//                                  @RequestParam("thanhTien") String thanhTien,
//                                  @RequestParam(value = "xuTichDiem", defaultValue = "false"
//                                        ) Boolean xuTichDiem) {
//        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
//        if (idNhanVien == null){
//            return "redirect:/login";
//        }
//
//        String uniqueTransactionId = vnPayUtil.getRandomNumber(8);
//        String returnUrl = "https://yourdomain.com/payment-return"; // Replace with your return URL
//
//        model.addAttribute("uniqueTransactionId", uniqueTransactionId);
//        model.addAttribute("returnUrl", returnUrl);
//
//
//        HoaDon hoaDon = banHangService.getOneById(Long.valueOf(idHoaDon));
//        BigDecimal tienGiamGia = banHangService.voucher(Long.valueOf(idHoaDon), BigDecimal.valueOf(Double.valueOf(tongTien)));
//        banHangService.checkXuHoaDon(Long.valueOf(idHoaDon), tongTien, thanhTien, xuTichDiem);
//        banHangService.thanhToanHoaDon(Long.valueOf(idHoaDon), tienGiamGia);
//        banHangService.updateGiamGia(Long.valueOf(idHoaDon));
//        banHangService.tichDiem(hoaDon.getKhachHang().getId(), thanhTien);
//        return "redirect:/admin/ban-hang";
//    }


    @GetMapping("/hoa-don/{idHoaDonCho}/huy-don")
    public String huyDon(@PathVariable("idHoaDonCho") String idHoaDon) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }
        banHangService.huyDon(Long.valueOf(idHoaDon));
        return "redirect:/admin/ban-hang";
    }


    @PostMapping("/thanh-toan/{idHoaDonCho}")
    public String thanhToanHoaDon( Model model,@PathVariable("idHoaDonCho") String idHoaDon,
                                   @RequestParam("tongTien") String tongTien,
                                   @RequestParam("thanhTien") String thanhTien,
                                   @RequestParam(value = "xuTichDiem", defaultValue = "false"
                                   ) Boolean xuTichDiem) {
        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }

        String uniqueTransactionId = vnPayUtil.getRandomNumber(8);
        String returnUrl = "https://yourdomain.com/payment-return"; // Replace with your return URL

        model.addAttribute("uniqueTransactionId", uniqueTransactionId);
        model.addAttribute("returnUrl", returnUrl);


        HoaDon hoaDon = banHangService.getOneById(Long.valueOf(idHoaDon));
        BigDecimal tienGiamGia = banHangService.voucher(Long.valueOf(idHoaDon), BigDecimal.valueOf(Double.valueOf(tongTien)));
        banHangService.checkXuHoaDon(Long.valueOf(idHoaDon), tongTien, thanhTien, xuTichDiem);
        banHangService.thanhToanHoaDon(Long.valueOf(idHoaDon), tienGiamGia);
        banHangService.updateGiamGia(Long.valueOf(idHoaDon));
        banHangService.tichDiem(hoaDon.getKhachHang().getId(), thanhTien);
        return "redirect:/admin/ban-hang";
    }


    @GetMapping("/vnPay/{idHoaDonCho}")
    public String thanhToanHoaDon(Model model, HttpServletRequest request,
                                  @PathVariable("idHoaDonCho") String idHoaDon,
                                  @RequestParam(value = "orderInfo", required = false, defaultValue = "Thanh toán hóa đơn ") String orderInfo,
//                                  @RequestParam("tongTienHang") String tongTien, @RequestParam("xuTichDiem") String xuTichDiem,  @RequestParam(value = "orderInfo", defaultValue = "Thanh toán hóa đơn ") String orderInfo,) {
                                  @RequestParam("tongTien") String tongTien,
                                  @RequestParam("xuTichDiemAmount") String xuTichDiemAmount,
                                  @RequestParam("thanhTien") String thanhTien,
                                  @RequestParam(value = "xuTichDiem", defaultValue = "false"
                                  ) Boolean xuTichDiem) {

        Long idNhanVien = spingsecurity.getCurrentNhanVienId();
        if (idNhanVien == null){
            return "redirect:/login";
        }


        System.out.println("ALl values 1 " + idHoaDon + " " + orderInfo + " " + tongTien     + " " + thanhTien + " " + xuTichDiem);



        HttpSession session = request.getSession();
        session.setAttribute("idHoaDon", idHoaDon);
        session.setAttribute("tongTien", tongTien);
        session.setAttribute("thanhTien", thanhTien);
        session.setAttribute("xuTichDiem", xuTichDiem);



        int parseTongTien = (int) Double.parseDouble(tongTien) ;

        int orderTotal;

        int discount = 50000;

        if(xuTichDiem) {

//            chon tich diem



            if((int) Double.parseDouble(xuTichDiemAmount) >= discount ) {

                orderTotal = parseTongTien   - discount;
            } else {

                orderTotal = parseTongTien - (int) Double.parseDouble(xuTichDiemAmount);
            }


        } else {

//            khong chon tich diem
            orderTotal = parseTongTien;
        }


        VNPayResponse vnpayUrl = paymentService.createVnPayPayment(request, orderTotal, orderInfo, vnpReturnUrl);
        String urlReturn = vnpayUrl.paymentUrl;
        return "redirect:" + urlReturn;

    }

}
