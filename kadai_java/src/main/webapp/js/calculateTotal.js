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
    console.log("calculateTotal 関数が呼び出されました");

    $("tr").each(function() {
        var quantity = $(this).find("select[name='quantity']").val();
        var price = $(this).find("td:eq(1)").text().trim();

        console.log('処理中の要素:', $(this));
        console.log('取得した数量:', quantity);
        console.log('取得した価格:', price);
        
        // 数量と価格を掛けて合計に追加
        if (quantity && price) {
            total += parseInt(quantity) * parseFloat(price);
        }
    });
    
    // 合計金額を整数に変換（小数点以下切り捨て）
    /*total = Math.floor(total);*/  // または `parseInt(total)` でもOK
    total = parseInt(total);

    console.log("合計金額は", total);

    // 合計金額を表示
    $("#totalAmount").text("合計金額: " + total + " 円");
}

