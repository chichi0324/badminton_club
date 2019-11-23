window.onload = function () {
	if(document.getElementById("show_message")!=null){
		document.getElementById("show_message").click();
		document.getElementById("show_message").style.display="none";
	}
	/* ------------------------星星評分------------------ */
    var s = document.getElementById("pingStar"),
      m = document.getElementById('dir'),
      n = s.getElementsByTagName("label"),
      input = document.getElementById('startP'),
      content=new Array('特别差，给1分','很差，给2分','一般般，给3分','很好，给4分','非常好，给5分'); //保存所选值

    
    clearAll = function () {
      for (var i = 0; i < n.length; i++) {
        n[i].className = '';
        n[i].innerHTML='☆';
      }
    }
    for (var i = 0; i < n.length; i++) {
      n[i].onclick = function () {
        var q = this.getAttribute("id");
        clearAll();
        input.value = q;
        for (var i = 0; i < q; i++) {
          n[i].className = 'on';
          n[i].innerHTML='★';
        }
        m.innerHTML = content[q-1];
      }
      n[i].onmouseover = function () {
        var q = this.getAttribute("id");
        clearAll();
        for (var i = 0; i < q; i++) {
          n[i].className = 'on';
          n[i].innerHTML='★';
        }
      }
      n[i].onmouseout = function () {
        clearAll();
        for (var i = 0; i < input.value; i++) {
          n[i].className = 'on';
          n[i].innerHTML='★';
        }
      }
    }
    

  }