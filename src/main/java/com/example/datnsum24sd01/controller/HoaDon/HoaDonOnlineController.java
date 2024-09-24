package com.example.datnsum24sd01.controller.HoaDon;

import com.example.datnsum24sd01.entity.GioHang;
import com.example.datnsum24sd01.entity.GioHangChiTiet;
import com.example.datnsum24sd01.entity.HoaDon;
import com.example.datnsum24sd01.enumation.TrangThai;
import com.example.datnsum24sd01.enumation.TrangThaiDonHang;
import com.example.datnsum24sd01.request.GioHangChiTietRequest;
import com.example.datnsum24sd01.responsitory.GioHangChiTietRepository;
import com.example.datnsum24sd01.responsitory.HoaDonRepo;
import com.example.datnsum24sd01.responsitory.KhachHangResponsitory;
import com.example.datnsum24sd01.service.ChiTietSanPhamService;
import com.example.datnsum24sd01.service.GioHangChiTietService;
import com.example.datnsum24sd01.service.HoaDonChiTietService;
import com.example.datnsum24sd01.service.HoaDonService;
import com.example.datnsum24sd01.service.KhachHangService;
import com.example.datnsum24sd01.worker.Spingsecurity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/beestore")
public class HoaDonOnlineController {

    @Autowired
    HttpServletRequest request;


    private final HoaDonService hoaDonService;
    private final HoaDonChiTietService hoaDonChiTietService;
    private final GioHangChiTietService gioHangChiTietService;
    private final ChiTietSanPhamService chiTietSanPhamService;
    private final KhachHangService khachHangService;
    private final GioHangChiTietRepository gioHangChiTietRepository;
    private Spingsecurity spingsecurity = new Spingsecurity();
    private final HoaDonRepo hoaDonRepo;


    public  HoaDonOnlineController (HoaDonService hoaDonService, HoaDonChiTietService hoaDonChiTietService, GioHangChiTietService gioHangChiTietService, ChiTietSanPhamService chiTietSanPhamService, KhachHangService khachHangService, KhachHangResponsitory khachHangRepository, GioHangChiTietRepository gioHangChiTietRepository, HoaDonRepo hoaDonRepo) {
        this.hoaDonService = hoaDonService;
        this.hoaDonChiTietService = hoaDonChiTietService;
        this.gioHangChiTietService = gioHangChiTietService;
        this.chiTietSanPhamService = chiTietSanPhamService;
        this.khachHangService = khachHangService;
        this.gioHangChiTietRepository = gioHangChiTietRepository;
        this.hoaDonRepo = hoaDonRepo;
    }

    List<TrangThaiDonHang> list = new ArrayList<>(Arrays.asList(TrangThaiDonHang.CHO_XAC_NHAN, TrangThaiDonHang.HOAN_THANH.DANG_CHUAN_BI,
            TrangThaiDonHang.DANG_GIAO, TrangThaiDonHang.DA_GIAO, TrangThaiDonHang.HOAN_THANH, TrangThaiDonHang.DA_HUY, TrangThaiDonHang.XAC_NHAN_TRA_HANG));

    /**
     * Get HoaDon By KhachHang
     *
     * @param model
     * @return
     */
    @GetMapping("/hoa-don-cua-toi")
    public String getAll(Model model) {

        Long id = spingsecurity.getCurrentUserId();
        if (id == null) {
            return "redirect:/login";
        }
        model.addAttribute("hoadons", hoaDonService.getAllByKhachHang(id));
        model.addAttribute("trangThais", list);
        return "customer-template/hoa_don";
    }

    /**
     * Get HoaDon By KhachHang and TrangThai
     *
     * @param model
     * @param trangThai
     * @return
     */
    @GetMapping("hoa-don-cua-toi/{trangThai}")
    public String getByTrangThai(Model model,
                                 @PathVariable("trangThai") TrangThaiDonHang trangThai) {
        Long id = spingsecurity.getCurrentUserId();
        if (id == null) {
            return "redirect:/login";
        }
        model.addAttribute("trangThais", list);
        model.addAttribute("hoadons", hoaDonService.getByTrangThaiAndKhachHang(trangThai, id));
        return "customer-template/hoa_don";
    }

    /**
     * Get ChiTietHoaDon by KhachHang
     *
     * @param model
     * @param idHd
     * @return
     */
    @GetMapping("/chi-tiet-hoa-don/{idHd}")
    public String detail(Model model,
                         @PathVariable("idHd") Long idHd
    ) {
        Long id = spingsecurity.getCurrentUserId();
        if (id == null) {
            return "redirect:/login";
        }
//        model.addAttribute("giamGia", hoaDonService.maGiamGia(idHd));
        model.addAttribute("newGhct", new GioHangChiTietRequest());
        model.addAttribute("hd", hoaDonService.findById(idHd));
        model.addAttribute("gioHangChiTiets", gioHangChiTietService.findGioHangChiTietById(idHd));
        return "customer-template/chi_tiet_hoa_don";
    }

    /**
     * Yêu Cầu Hoàn Trả và số Lượng Sản phẩm hoàn trả
     *
     * @param gioHangChiTietRequest
     * @param idGhct
     * @param idHd
     * @return
     */
    @PostMapping("/put/{id}/{idHd}")
    public String hoanTra(@Valid @ModelAttribute("newGhct") GioHangChiTietRequest gioHangChiTietRequest,
                          Model model,
                          @PathVariable("id") Long idGhct,
                          @PathVariable("idHd") Long idHd) {
        GioHangChiTiet gioHangChiTiet = gioHangChiTietService.getOne(idGhct).get();
        HoaDon hoaDon = hoaDonService.findById(idHd);
        Long id = spingsecurity.getCurrentUserId();
        if (id == null) {
            return "redirect:/login";
        }

        if (gioHangChiTietRequest.getSoLuong() == null) {
            model.addAttribute("idKh", gioHangChiTiet.getHoaDon().getKhachHang().getId().toString());
            model.addAttribute("newGhct", new GioHangChiTietRequest());
            model.addAttribute("hd", hoaDonService.findById(idHd));
            model.addAttribute("gioHangChiTiets", gioHangChiTietService.findGioHangChiTietById(idHd));
            model.addAttribute("err", "Bạn phải nhập số lượng!");
            return "customer-template/chi_tiet_hoa_don";
        }

        if (gioHangChiTietRequest.getSoLuong() > gioHangChiTiet.getSoLuong()) {
            model.addAttribute("idKh", gioHangChiTiet.getHoaDon().getKhachHang().getId().toString());
            model.addAttribute("newGhct", new GioHangChiTietRequest());
            model.addAttribute("hd", hoaDonService.findById(idHd));
            model.addAttribute("gioHangChiTiets", gioHangChiTietService.findGioHangChiTietById(idHd));
            model.addAttribute("err", "Số lượng trả hàng không thể lớn hơn số lượng mua!");
            return "customer-template/chi_tiet_hoa_don";
        }


        if (gioHangChiTietRequest.getGhiChu().isEmpty()) {
            model.addAttribute("idKh", gioHangChiTiet.getHoaDon().getKhachHang().getId().toString());
            model.addAttribute("newGhct", new GioHangChiTietRequest());
            model.addAttribute("hd", hoaDonService.findById(idHd));
            model.addAttribute("gioHangChiTiets", gioHangChiTietService.findGioHangChiTietById(idHd));
            model.addAttribute("err", "Lý do trả hàng không được để trống!");
            return "customer-template/chi_tiet_hoa_don";
        }

        if (gioHangChiTiet.getHoaDon().getTrangThai() == TrangThaiDonHang.HOAN_THANH) {
            return "redirect:/beestore/chi-tiet-hoa-don/" + idHd;
        }
        for (GioHangChiTiet gioHangChiTiet2 : gioHangChiTietService.findGioHangChiTietById(idHd)) {
            if (gioHangChiTiet2.getTrangThai().equals(TrangThai.YEU_CAU_TRA_HANG)
                    && gioHangChiTiet.getChiTietSanPham().getId().equals(gioHangChiTiet2.getChiTietSanPham().getId())
                    && gioHangChiTietRequest.getSoLuong() == gioHangChiTiet.getSoLuong()) {
                gioHangChiTiet2.setSoLuong(gioHangChiTietRequest.getSoLuong()+gioHangChiTiet2.getSoLuong());
                gioHangChiTiet2.setDonGia(gioHangChiTiet.getDonGia());
                gioHangChiTietRepository.delete(gioHangChiTiet);
                gioHangChiTietService.save(gioHangChiTiet2);
                return "redirect:/beestore/chi-tiet-hoa-don/" + idHd + "?success";
            }
        }
        if (gioHangChiTietRequest.getSoLuong() == gioHangChiTiet.getSoLuong()) {
            hoaDon.setTrangThai(TrangThaiDonHang.XAC_NHAN_TRA_HANG);
            hoaDonService.save(hoaDon);
            gioHangChiTiet.setGhiChu(gioHangChiTietRequest.getGhiChu());
            gioHangChiTiet.setTrangThai(TrangThai.YEU_CAU_TRA_HANG);
            gioHangChiTietService.save(gioHangChiTiet);
            return "redirect:/beestore/chi-tiet-hoa-don/" + idHd + "?success";
        }
        hoaDon.setTrangThai(TrangThaiDonHang.XAC_NHAN_TRA_HANG);
        hoaDonService.save(hoaDon);
        gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong() - gioHangChiTietRequest.getSoLuong());
        gioHangChiTietService.save(gioHangChiTiet);

        GioHangChiTiet gioHangChiTiet1 = new GioHangChiTiet();
        gioHangChiTiet1.setChiTietSanPham(gioHangChiTiet.getChiTietSanPham());
        gioHangChiTiet1.setHoaDon(gioHangChiTiet.getHoaDon());
        gioHangChiTiet1.setGioHang(gioHangChiTiet.getGioHang());
        gioHangChiTiet1.setDonGia(gioHangChiTiet.getDonGia());
        gioHangChiTiet1.setNgayTao(LocalDate.now());
        gioHangChiTiet1.setSoLuong(gioHangChiTietRequest.getSoLuong());
        gioHangChiTiet1.setGhiChu(gioHangChiTietRequest.getGhiChu());
        gioHangChiTiet1.setTrangThai(TrangThai.YEU_CAU_TRA_HANG);
        for (GioHangChiTiet gioHangChiTiet2 : gioHangChiTietService.findGioHangChiTietById(idHd)) {

            if (gioHangChiTiet2.getTrangThai().equals(TrangThai.YEU_CAU_TRA_HANG)
                    && gioHangChiTiet.getChiTietSanPham().getId().equals(gioHangChiTiet2.getChiTietSanPham().getId())) {


                int tongSoLuong = gioHangChiTiet2.getSoLuong() + gioHangChiTietRequest.getSoLuong();
                gioHangChiTiet2.setSoLuong(tongSoLuong);
                gioHangChiTiet2.setGhiChu(gioHangChiTietRequest.getGhiChu());
                gioHangChiTietService.save(gioHangChiTiet2);
                return "redirect:/beestore/chi-tiet-hoa-don/" + idHd + "?success";
            }
        }
        gioHangChiTietService.save(gioHangChiTiet1);
        return "redirect:/beestore/chi-tiet-hoa-don/" + idHd + "?success";
    }


    @PostMapping("/return/{id}/{idHd}")
    public String DoiHang(@Valid @ModelAttribute("newGhct") GioHangChiTietRequest gioHangChiTietRequest,
                          @PathVariable("id") Long idGhct,
                          Model model,
                          @PathVariable("idHd") Long idHd) {
        Long id = spingsecurity.getCurrentUserId();
        if (id == null) {
            return "redirect:/login";
        }
        GioHangChiTiet gioHangChiTiet = gioHangChiTietService.getOne(idGhct).get();
        HoaDon hoaDon = hoaDonService.findById(idHd);
        if (gioHangChiTietRequest.getGhiChu().isEmpty()) {
            model.addAttribute("idKh", gioHangChiTiet.getHoaDon().getKhachHang().getId().toString());
            model.addAttribute("newGhct", new GioHangChiTietRequest());
            model.addAttribute("hd", hoaDonService.findById(idHd));
            model.addAttribute("gioHangChiTiets", gioHangChiTietService.findGioHangChiTietById(idHd));
            model.addAttribute("err", "Ghi chú  đơn hàng không được để trống!");
            return "customer-template/chi_tiet_hoa_don";
        }

        if (gioHangChiTiet.getHoaDon().getTrangThai() == TrangThaiDonHang.HOAN_THANH) {
            return "redirect:/beestore/chi-tiet-hoa-don/" + idHd;
        }
        gioHangChiTiet.setTrangThai(TrangThai.DOI_HANG);
        hoaDon.setTrangThai(TrangThaiDonHang.DOI_HANG);
        gioHangChiTietService.save(gioHangChiTiet);
        hoaDonService.save(hoaDon);
        return "redirect:/beestore/chi-tiet-hoa-don/" + idHd + "?success";
    }

//    @PostMapping("/validation/deny")
//    public String validationDeny(@Param("id") Long id,
//                                 @RequestParam("ghiChu") String ghichu,
//                                 Model model
//    ) {
//        Long idKh = spingsecurity.getCurrentUserId();
//        HoaDon hoaDon = hoaDonService.findById(id);
//
//        if (idKh == null) {
//            return "redirect:/login";
//        }
//        if (ghichu.isEmpty()) {
//            model.addAttribute("err", "Ghi chú  đơn hàng không được để trống!");
//            model.addAttribute("hoadons", hoaDonService.getAll());
//            model.addAttribute("trangThais", list);
//            return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?success";
//        }
//        if (hoaDon != null) {
//            if(hoaDon.getTrangThai() != TrangThaiDonHang.CHO_XAC_NHAN){
//                return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?success";
//            }
//            if(hoaDon.getTrangThai() != TrangThaiDonHang.DANG_CHUAN_BI){
//                return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?success";
//            }
//            hoaDonService.validate(hoaDon, TrangThaiDonHang.DA_HUY, ghichu);
//            return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?success";
//        }
//        return null;
//    }
@PostMapping("/validation/deny")
public String validationDeny(@RequestParam("id") Long id,
                             @RequestParam("ghiChu") String ghichu,
                             Model model) {
    Long idKh = spingsecurity.getCurrentUserId();
    HoaDon hoaDon = hoaDonService.findById(id);

    if (idKh == null) {
        return "redirect:/login";
    }
    if (ghichu.isEmpty()) {
        model.addAttribute("err", "Ghi chú đơn hàng không được để trống!");
        model.addAttribute("hoadons", hoaDonService.getAll());
        model.addAttribute("trangThais", list);
        return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?error";
    }
    if (hoaDon != null) {
        // Check if the order status is either CHO_XAC_NHAN or DANG_CHUAN_BI
        if (hoaDon.getTrangThai() == TrangThaiDonHang.CHO_XAC_NHAN ||
                hoaDon.getTrangThai() == TrangThaiDonHang.DANG_CHUAN_BI) {
            hoaDonService.validate(hoaDon, TrangThaiDonHang.DA_HUY, ghichu);
            return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?success";
        }
        // If status is not valid for cancellation, redirect with an error
        model.addAttribute("err", "Đơn hàng không thể bị hủy ở trạng thái hiện tại!");
        model.addAttribute("hoadons", hoaDonService.getAll());
        model.addAttribute("trangThais", list);
        return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?error";
    }
    return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?error";
}

    @PostMapping("/validation")
    public String validation(@Param("id") Long id
    ) {
        Long idKh =spingsecurity.getCurrentUserId();
        if (idKh == null) {
            return "redirect:/login";
        }
        HoaDon hoaDon = hoaDonService.findById(id);
        if (hoaDon != null) {
            hoaDonService.validate(hoaDon, TrangThaiDonHang.HOAN_THANH, "");
            return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?success";
        }
        return "redirect:/beestore/chi-tiet-hoa-don/" + id + "?success";
    }

    @GetMapping("/tra-cuu-don-hang")
    public String view(Model model) {
        Long id = spingsecurity.getCurrentUserId();
        Boolean checkSecurity = false;
        if (id != null) {
            checkSecurity = true;
        }
        model.addAttribute("checkSecurity", checkSecurity);
        return "customer-template/tra_cuu";

    }

    @GetMapping("/thong-tin-tra-cuu-don-hang")
    public String traCuuDonHang(@RequestParam(value = "maHd", required = false) String maHd, Model model) {
        Long id = spingsecurity.getCurrentUserId();
        Boolean checkSecurity = false;
        if (id != null) {
            checkSecurity = true;
        }
        model.addAttribute("checkSecurity", checkSecurity);
        if (hoaDonRepo.existsByMa(maHd.trim())) {
            HoaDon hoaDon = hoaDonService.findByMa(maHd.trim());
            model.addAttribute("giamGia", hoaDonService.maGiamGia(hoaDon.getId()));
            model.addAttribute("hd", hoaDonService.findById(hoaDon.getId()));
            model.addAttribute("gioHangChiTiets", gioHangChiTietService.findGioHangChiTietById(hoaDon.getId()));
            return "customer-template/hoadon/tra_cuu1";
        } else {
            model.addAttribute("err", "Hoá đơn bạn nhập không tồn tại!");
            return "customer-template/tra_cuu";
        }
    }
    @GetMapping("/don-hang/thong-tin-thanh-toan")
    public RedirectView thongTinThanhToan() {

        try {


            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = (String) params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }
            HoaDon donhang = hoaDonService.findById(Long.valueOf(request.getParameter("vnp_TxnRef")));
            if (donhang != null) {
                if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                    System.out.println("Thanh toán thành công ");
                    donhang.setTrangThai(TrangThaiDonHang.CHO_XAC_NHAN);
                    hoaDonService.save(donhang);
                    return new RedirectView("http://localhost:8080/index#/success");
                } else {
//                    hdctService.xoaHDCTByIdDonHang(donhang);
//                    hoaDonService.xoaDonHang(donhang);
                    System.out.println("Không thành công");
                    return new RedirectView("http://localhost:8080/index#/fail");
                }
            } else {
                System.out.println("Không tìm thấy order");
                return new RedirectView("http://localhost:8080/index#/fail");
            }
        } catch (Exception e) {
            System.out.println("k xdc");
            return new RedirectView("http://localhost:8080/index#/fail");
        }
    }
}
