package com.example.kasirnaspad.data

class KasirRepository (
    private val menuDao: menuDao,
    private val transaksiDao: TransaksiDao,
    private val detailTransaksiDao: DetailTransaksiDao
){
    val semuaMenu = menuDao.getAllMenu()
    val semuaTransaksi = transaksiDao.getAllTransaksi()

    suspend fun tambahMenu(menu: Menu){
        menuDao.insertMenu(menu)
    }

    fun getTransaksiByMetode(metode: String) = transaksiDao.getTransaksiByMetode(metode)
    fun getTotalByMetode(metode: String) = transaksiDao.getTotalByMetode(metode)

    suspend fun prosesCheckout(
        itemKeranjang: List<Pair<Menu, Int>>,
        metodePembayaran: String
    ){
        val totalHarga = itemKeranjang.sumOf { (menu, jumlah) -> menu.harga * jumlah }

        val transaksi = Transaksi(
            TanggalWaktu = System.currentTimeMillis(),
            TotalHarga = totalHarga,
            MetodePembayaran = metodePembayaran
        )
        val transaksiId = transaksiDao.insertTransaksi(transaksi)

        itemKeranjang.forEach { (menu, jumlah) ->
            val detail = DetailTransaksi(
                idTransaksi = transaksiId.toInt(),
                idMenu = menu.id,
                jumlah = jumlah,
                subtotal = menu.harga * jumlah
            )
            detailTransaksiDao.insertDetailTransaksi(detail)
            menuDao.kurangiStok(menu.id, jumlah)
        }
    }
}