// calculateTotal.js

$(document).ready(function() {
    // 初期化
    calculateTotal();

    // 数量の変更イベント
    $("select[name='quantity']").change(function() {
        calculateTotal();
    });
});

function calculateTotal() {
    var total = 0;

    $("tr").each(function() {
        var quantity = $(this).find("select[name='quantity']").val();
        var price = $(this).find("td:eq(1)").text();
        
        // 数量と価格を掛けて合計に追加
        if (quantity && price) {
            total += parseInt(quantity) * parseFloat(price);
        }
    });

    // 合計金額を表示
    $("#totalAmount").text("合計金額: " + total.toFixed(2) + " 円");
}
