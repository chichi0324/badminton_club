var i = 0;
    $(function () {

      $('#download').css('visibility', 'hidden');


      $("#btnStartShotImage").click(function () {
        //显示裁剪图片的弹窗
        $('#myModal').modal('show');
      });

      $("#getPicture").click(function () {
        $('#myModal').modal('hide');
        $('#download').css('visibility', 'visible');
      });

      $("#btnStartShotImage").click(function () {
        if (i == 0) {
          $('#full_image').css('visibility', 'hidden');
        } 
        i = i + 1;
      });

      $("#inputImage").click(function () {
        $('#full_image').css('visibility', 'visible');
      });


    });