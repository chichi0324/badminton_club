// -------------------其他需要的報名資料部分------------------- 
    var i=0;
    function addItem() {
    	i=i+1;
      var itemDiv = document.getElementsByClassName("item_data")[0].cloneNode(true);
      itemDiv.getElementsByClassName("input_item")[0].value = "";
      itemDiv.getElementsByClassName("input_item")[0].id = "otherDatas"+i;
      itemDiv.getElementsByClassName("input_item")[0].name = "otherDatas["+i+"]";
      itemDiv.getElementsByClassName("btn_del")[0].onclick = removeItem;

      var start_item = document.getElementById("start_item");
      start_item.insertBefore(itemDiv, start_item.childNodes[0]);
    }

    function removeItem(e) {
 	
      var itemDiv = e.target.parentNode;
      console.log(document.getElementsByClassName("item_data").length);
      if (document.getElementsByClassName("item_data").length > 1) {
        document.getElementById("start_item").removeChild(itemDiv);
      } else {
        alert("至少要有一個項目")
      }

    }

    window.addEventListener("load", function () {
      document.getElementById("add_item").onclick = addItem;
      document.getElementsByClassName("btn_del")[0].onclick = removeItem;
    }, false);
    
 // ---------------------------------------------------------
    //勾選 [可攜帶親友參加 ]，才顯示[需要親友資料]
    function getFriend() {
        if(document.getElementsByName("enableFriend")[0].checked == true){
            document.getElementById("needFriendData").style.display = "block";
        }else{
        	document.getElementsByName("friendData")[0].checked = false;
            document.getElementById("needFriendData").style.display = "none";          
        }
    }

    // -------------------活動宣傳圖預覽---------------------
    $(function () {
      function preview1(input) {
        if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function (e) {
            $('.img1').attr('src', e.target.result);
          }
          reader.readAsDataURL(input.files[0]);
        }
      }
      function preview2(input) {
        if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function (e) {
            $('.img2').attr('src', e.target.result);
          }
          reader.readAsDataURL(input.files[0]);
        }
      }
      function preview3(input) {
        if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function (e) {
            $('.img3').attr('src', e.target.result);
          }
          reader.readAsDataURL(input.files[0]);
        }
      }
      function preview4(input) {
        if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function (e) {
            $('.img4').attr('src', e.target.result);
          }
          reader.readAsDataURL(input.files[0]);
        }
      }
      function preview5(input) {
        if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function (e) {
            $('.img5').attr('src', e.target.result);
          }
          reader.readAsDataURL(input.files[0]);
        }
      }

      $("#uplImg1").change(function (input) {
        preview1(this);
      })
      $("#uplImg2").change(function (input) {
        preview2(this);
      })
      $("#uplImg3").change(function (input) {
        preview3(this);
      })
      $("#uplImg4").change(function (input) {
        preview4(this);
      })
      $("#uplImg5").change(function (input) {
        preview5(this);
      })
      
      //勾選親友顯示部分
     getFriend();
      
      //錯誤訊息及提醒視窗部分
		if(document.getElementById("show_message")!=null){
			document.getElementById("show_message").click();
			document.getElementById("show_message").style.display="none";
		}
      
    })