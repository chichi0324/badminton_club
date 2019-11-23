

    function add(){
      var number=document.getElementById("joinNumber").value;
      document.getElementById("joinNumber").value=parseInt(number)+1;
      if(document.getElementById("joinNumber").value>1){
    	  document.getElementById("next").style.display='block';
    	  document.getElementById("apply").style.display='none';
      }
    }

    function less(){
      var number=document.getElementById("joinNumber").value;
      if(number>1){
        document.getElementById("joinNumber").value=parseInt(number)-1;
      }
      if(document.getElementById("joinNumber").value==1){
    	  document.getElementById("next").style.display='none';
    	  document.getElementById("apply").style.display='block';
      }
    }

    window.addEventListener("load", function () {
      document.getElementById("add").onclick = add;
      document.getElementById("less").onclick = less;
      
      if(document.getElementById("show_message")!=null){
			document.getElementById("show_message").click();
			document.getElementById("show_message").style.display="none";
		}
    },false);