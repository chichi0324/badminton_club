$(document).ready(function () {

    // ------------------ckeck.html和member.html------------------
    $("#CheckAll").click(function () {
        if ($("#CheckAll").prop("checked")) {//如果全選按鈕有被選擇的話（被選擇是true）
            $("input[class='checxkBoxTick']").each(function () {
                $(this).prop("checked", true);//把所有的核取方框的property都變成勾選
            })
        } else {
            $("input[class='checxkBoxTick']").each(function () {
                $(this).prop("checked", false);//把所有的核方框的property都取消勾選
            })
        }
    })

    // ------------------permission.html------------------

    $("#CheckAll_1").click(function () {
        if ($("#CheckAll_1").prop("checked")) {//如果全選按鈕有被選擇的話（被選擇是true）
            $("input[name='Checkbox_1']").each(function () {
                $(this).prop("checked", true);//把所有的核取方框的property都變成勾選
            })
        } else {
            $("input[name='Checkbox_1']").each(function () {
                $(this).prop("checked", false);//把所有的核方框的property都取消勾選
            })
        }
    })

    $("#CheckAll_2").click(function () {
        if ($("#CheckAll_2").prop("checked")) {//如果全選按鈕有被選擇的話（被選擇是true）
            $("input[name='Checkbox_2']").each(function () {
                $(this).prop("checked", true);//把所有的核取方框的property都變成勾選
            })
        } else {
            $("input[name='Checkbox_2']").each(function () {
                $(this).prop("checked", false);//把所有的核方框的property都取消勾選
            })
        }
    })

    $("#CheckAll_3").click(function () {
        if ($("#CheckAll_3").prop("checked")) {//如果全選按鈕有被選擇的話（被選擇是true）
            $("input[name='Checkbox_3']").each(function () {
                $(this).prop("checked", true);//把所有的核取方框的property都變成勾選
            })
        } else {
            $("input[name='Checkbox_3']").each(function () {
                $(this).prop("checked", false);//把所有的核方框的property都取消勾選
            })
        }
    })



})


