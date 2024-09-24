Create Database DATN_SUM24_SD01;
use DATN_SUM24_SD01;

-- 1. Nhà Cung Cấp
create table nha_cung_cap
(
    id         bigint auto_increment
        primary key,
    ma         varchar(255) not null,
    ngay_sua   date null,
    ngay_tao   date null,
    ten        varchar(255) not null,
    trang_thai tinyint null,
    constraint UK_28w4x0up9146ir61vu5nj6gs4
        unique (ma),
    constraint UK_ti2brqn4tp0othp3iwu97jwtp
        unique (ten)
);

-- 2.Thương Hiệu
create table thuong_hieu
(
    id         bigint auto_increment
        primary key,
    ma         varchar(255) not null,
    ngay_sua   date null,
    ngay_tao   date null,
    ten        varchar(255) not null,
    trang_thai tinyint null,
    constraint UK_123sssxf1x7ax6jyo4wdcvk36
        unique (ma),
    constraint UK_53w31rjwmistgvxj1lk2gnus9
        unique (ten)
);

-- 3.Sản Phẩm
create table san_pham
(
    id               bigint auto_increment
        primary key,
    anh_chinh        varchar(255) null,
    ma               varchar(255) not null,
    mo_ta            varchar(255) null,
    ngay_sua         date null,
    ngay_tao         date null,
    ten              varchar(255) null,
    trang_thai       tinyint null,
    id_nha_cung_cap bigint null,
    id_thuong_hieu   bigint null,
    constraint UK_131y1wu003wtcyqns9naaabhr
        unique (ma),
    constraint FK5266y3xb83fch2ygdg6wf58qu
        foreign key (id_thuong_hieu) references thuong_hieu (id),
    constraint FKlds313o255x6s8a6cj58lo5hj
        foreign key (id_nha_cung_cap) references nha_cung_cap (id)
);

-- 4.Ảnh
create table anh
(
    id          bigint auto_increment
        primary key,
    ten         varchar(255) null,
    url         varchar(255) null,
    id_san_pham bigint null,
    constraint FK2dp9xg8gpf8t1jfw4wxb35o7y
        foreign key (id_san_pham) references san_pham (id)
);

-- 5.Đế Giày
create table de_giay
(
    id  bigint auto_increment
        primary key,
    ma  varchar(255) not null,
    ten varchar(255) not null,
    ngay_sua   date null,
    ngay_tao   date null,
    constraint UK_511l83jif9dfpiiqfkaa4ofyi
        unique (ten),
    constraint UK_bse8cvun3iodgds58ox9h8yhw
        unique (ma)
);

-- 6.Kích Thước
create table kich_thuoc
(
    id  bigint auto_increment
        primary key,
    ma  varchar(255) not null,
    ten varchar(255) not null,
    ngay_sua   date null,
    ngay_tao   date null,
    constraint UK_gkywxpxndarpnx0eo0efvw653
        unique (ten),
    constraint UK_qtq2dqyq8ut9lu7xir4c01y0u
        unique (ma)
);
-- 7.Màu Sắc
create table mau_sac
(
    id  bigint auto_increment
        primary key,
    ma  varchar(255) not null,
    ten varchar(255) not null,
    ngay_sua   date null,
    ngay_tao   date null,
    constraint UK_i8g6p3221tdj7b0i007uyx4uo
        unique (ma),
    constraint UK_obo04bgmvelnnhd3k64hgixj4
        unique (ten)
);


-- 8.Nhân Viên
create table nhan_vien
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) null,
    gioi_tinh  tinyint null,
    ma         varchar(255) null,
    mat_khau   varchar(255) null,
    ngay_sua   date null,
    ngay_tao   date null,
    sdt        varchar(255) null,
    ten        varchar(255) null,
    cccd        varchar(255) null,
    trang_thai tinyint null,
    constraint UK_9etpn19qmeos5n3dqc87qild3
        unique (ma),
    constraint UK_mafuwxhl2bcv6obb9fkouokec
        unique (sdt)
);

-- 9.Phiếu Giảm Giá
create table phieu_giam_gia
(
    id               bigint auto_increment
        primary key,
    gia_tri_don_hang decimal(65, 2) null,
    ma               varchar(255) null,
    mo_ta            varchar(255) null,
    muc_giam_gia     int null,
    ngay_bat_dau     date null,
    ngay_ket_thuc    date null,
    ngay_sua         date null,
    ngay_tao         date null,
    so_luong         int null,
    ten              varchar(255) null,
    trang_thai       tinyint null,
    muc_giam_toi_da  decimal(65, 2) null,
    constraint UK_acuss6gxp9w56b8rg3mmg3yux
        unique (ma)
);

-- 10.Chi tiết sản phẩm
create table chi_tiet_san_pham
(
    id            bigint auto_increment
        primary key,
    gia_ban       decimal(65, 2) null,
    gia_mac_dinh  decimal(65, 2) null,
    ngay_sua      date null,
    ngay_tao      date null,
    so_luong_ton  int null,
    trang_thai    tinyint null,
    id_de_giay    bigint null,
    id_kich_thuoc bigint null,
    id_mau_sac    bigint null,
    id_san_pham   bigint null,
    constraint FK5hacuc63k9pfld0eomf5vnrlj
        foreign key (id_kich_thuoc) references kich_thuoc (id),
    constraint FKhrc41nqmp3jsh42ikergp7qsd
        foreign key (id_mau_sac) references mau_sac (id),
    constraint FKhry1oewlwwhwhuqhr1tinw6l6
        foreign key (id_san_pham) references san_pham (id),
    constraint FKtj2f11b2f5l0l8rh9wfnyena2
        foreign key (id_de_giay) references de_giay (id)
);

-- 11.Khách hàng
create table khach_hang
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) null,
    gioi_tinh  tinyint null,
    ma         varchar(255) null,
    mat_khau   varchar(255) null,
    ngay_sua   date null,
    ngay_tao   date null,
    sdt        varchar(255) null,
    ten        varchar(255) null,
    trang_thai tinyint null,
    tich_diem  decimal(65, 2) null,
    constraint UK_1lmmis0qdveete6l4prc9xlad
        unique (ma),
    constraint UK_6gn74xxiy11yxkbb2xmldnlld
        unique (sdt)
);

-- 12.Địa Chỉ
create table dia_chi
(
    id             bigint auto_increment
        primary key,
    dia_chi        varchar(255) null,
    ghi_chu        varchar(255) null,
    ngay_sua       date null,
    ngay_tao       date null,
    sdt            varchar(255) null,
    ten            varchar(255) null,
    ten_nguoi_nhan varchar(255) null,
    phuong_xa      varchar(255) null,
    quan_huyen     varchar(255) null,
    thanh_pho      varchar(255) null,
    trang_thai     tinyint null,
    id_khach_hang  bigint null,
    constraint FKrk60uo19d67v1wpbp5a1rmao5
        foreign key (id_khach_hang) references khach_hang (id)
);

-- 13.Giỏ Hàng
create table gio_hang
(
    id            bigint auto_increment
        primary key,
    ma            varchar(255) null,
    ngay_sua      date null,
    ngay_tao      date null,
    trang_thai    tinyint null,
    id_khach_hang bigint null,
    constraint UK_q7fhxt1ya2dvmjcrrkxisvpl5
        unique (id_khach_hang),
    constraint UK_s20okvj8cy9ux6taewckmju9
        unique (ma),
    constraint FK6c8iirgeg8qcwpq1oa9noxshw
        foreign key (id_khach_hang) references khach_hang (id)
);

-- 14.Hóa Đơn
create table hoa_don
(
    id              bigint auto_increment
        primary key,
    dia_chi         varchar(255) null,
    ghi_chu         varchar(255) null,
    loai_hoa_don    tinyint null,
    ma              varchar(255) null,
    ngay_giao       date null,
    ngay_sua        date null,
    ngay_tao        date null,
    ngay_thanh_toan date null,
    phi_van_chuyen  decimal(65, 2) null,
    sdt             varchar(255) null,
    ten_khach_hang  varchar(255) null,
    thanh_toan      decimal(65, 2) null,
    tong_tien       decimal(65, 2) null,
    trang_thai      tinyint null,
    id_khach_hang   bigint null,
    id_nhan_vien    bigint null,
    id_phieu_giam_gia  bigint null,
    xu              decimal(65, 2) null,
    tien_giam_gia   decimal(65, 2) null,
    constraint UK_qc99tpq7eqghgm22o8e06gqyf
        unique (ma),
    constraint FK8t4ha4ehtva0djgtpn7ljexce
        foreign key (id_phieu_giam_gia) references phieu_giam_gia (id),
    constraint FKkuxkrkgq8yftm4d8d7o0w6nbv
        foreign key (id_nhan_vien) references nhan_vien (id),
    constraint FKrygimdf5nr1g2t6u03gvtr1te
        foreign key (id_khach_hang) references khach_hang (id)
);

-- 15.Hóa đơn chi tiết
create table hoa_don_chi_tiet
(
    id                   bigint auto_increment
        primary key,
    de_giay              varchar(255) null,
    gia_ban              decimal(65, 2) null,
    kich_thuoc           varchar(255) null,
    mau_sac              varchar(255) null,
    ngay_sua             date null,
    ngay_tao             date null,
    so_luong             int null,
    ten_san_pham         varchar(255) null,
    thuong_hieu          varchar(255) null,
    trang_thai           tinyint null,
    id_chi_tiet_san_pham bigint null,
    id_hoa_don           bigint null,
    constraint FK4672qsis193xo4polycwrcwb8
        foreign key (id_chi_tiet_san_pham) references chi_tiet_san_pham (id),
    constraint FK5adopt864mjisuy5xmgmy8iun
        foreign key (id_hoa_don) references hoa_don (id)
);

-- 16.Giỏ hàng chi tiết
create table gio_hang_chi_tiet
(
    id                   bigint auto_increment
        primary key,
    don_gia              decimal(65, 2) null,
    ghi_chu              varchar(255) null,
    ngay_sua             date null,
    ngay_tao             date null,
    so_luong             int null,
    trang_thai           tinyint null,
    id_chi_tiet_san_pham bigint null,
    id_gio_hang          bigint null,
    id_hoa_don           bigint null,
    constraint FKhkle2qtnnet5fq60x6tdhekql
        foreign key (id_gio_hang) references gio_hang (id),
    constraint FKlcvoteetgysdpfavfevmyh3en
        foreign key (id_chi_tiet_san_pham) references chi_tiet_san_pham (id),
    constraint FKr3utb9x7j00p1ghfj4mkhjyct
        foreign key (id_hoa_don) references hoa_don (id)
);

