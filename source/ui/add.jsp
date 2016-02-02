<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="alti" uri="http://www.alticast.com/alti/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="<c:url value = '/js/page.js'/>"></script>
<script type="text/javascript">
	/** main by onload. */
	main = function() {

		// 달력 셋팅
		setDate("#eventStartDate");
		setDate("#eventEndDate");

		// 이미지/텍스트 여부 셋팅
		setRadio("lstDescUseYn", $("input[name=lstDescUseYn]:checked").val());
		setRadio("contDescUseYn", $("input[name=contDescUseYn]:checked").val());

		// 이미지 태그 생성
		makeImageTag("#divBnrDisplay", "bnrImgRealFileRte", "bnrImgRealFileRte", "", bnrImgChangeFlagName);
		makeImageTag("#divLstDescImage", "lstImgFileRte", "lstImgFileRte", "", lstImgImgChangeFlagName);
		makeImageTag("#divContDescImage", "contImgFileRte", "contImgFileRte", "", contImgChangeFlagName);

		// 버튼 이벤트
		$(".conts-button").on("click", function() {
			var id = this.id;
			if (id == "addBtn") {
				doSubmit("add");
			} else if (id == "cancelBtn") {
				doSubmit("cancel");
			} else if (id == "cancelBtn") {
				doSubmit("cancel");
			}
		});

		// 플랫폼 체크 이벤트
		$(".conts-checkbox").on("click", setChkBox);

		// 라디오버튼 체크 이벤트
		$(".conts-radio").on("click", function() {
			var name = $(this).attr("name");
			var value = $(this).val();
			setRadio(name, value);
		});

		// 제공순위 이벤트
		$("select#ofrRnk").on("change", setRnkRewardByOfrRnk);
		setRnkRewardByOfrRnk();

		// 달력 날짜 변경 이벤트
		$("._input_calendar").on("change", setRewardByOfrRnk);
		
	};

	// 달력 날짜 선택에 따른 이벤트
	setRewardByOfrRnk = function() {

		$("#theadRewardList").html("");
		$("#tbodyRewardList").html("");

		var oneDay = 24*60*60*1000;
		var sttDate = new Date($("#eventStartDate").val());
		var endDate = new Date($("#eventEndDate").val());
		var diffDays = Math.round(Math.abs((sttDate.getTime() - endDate.getTime())/(oneDay))); // 총 일수.
		var ofrRnk = $("select#ofrRnk").val(); // 제공순위
		var tdHtml = "<td class='[0]'>[1]</td>";
		var ofrRnkHtml = "<input type='hidden' name='eventRewardList[{0}].ofrRnk' value='{1}' />";
		var yearHtml = "<input type='hidden' name='eventRewardList[{0}].eventDate' value='{1}' />";
		var rewardHtml = "1 / <input name='eventRewardList[{0}].winRateFomular' style='width: 60px;' /> (총 <input name='eventRewardList[{1}].maxWinPerson' style='width: 60px;'/> 명)";
		
		var theadHtml = "";
		var tbodyHtml = "";
		var rewardIdx = 0;
		for (var i = 0; i <= diffDays; i++) {

			// 날짜 값.
			var date = new Date();
			date.setDate(sttDate.getDate() + i);
			var newDate = $.datepicker.formatDate("yy-mm-dd", date);
			
			if (i == 0) {
				theadHtml += "<tr class='headTr'>";
			}
			tbodyHtml += "<tr class='contsTr center'>";
			
			for (var j = 1; j <= ofrRnk; j++) {
				// 첫번째 컬럼 생성
				if (j == 1) {
					if (i == 0) {
						theadHtml += tdHtml.replace("[0]", "firstCol").replace("[1]", "날짜 / 등수");
					}
					
					tbodyHtml += tdHtml.replace("[0]", "firstCol").replace("[1]",newDate);
				}

				// 컬럼의 class 명을 설정
				var tdColClass = "middleCol";
				if (j == ofrRnk) {
					tdColClass = "lastCol";
				}

				// 첫번째를 제외한 나머지 컬럼 생성
				if (i == 0) {
					theadHtml += tdHtml.replace("[0]", tdColClass).replace("[1]", j + " 등");
				}
				
				tbodyHtml += "<td class='" + tdColClass + "'>";
				tbodyHtml += ofrRnkHtml.replace("{0}", rewardIdx).replace("{1}", j);
				tbodyHtml += yearHtml.replace("{0}", rewardIdx).replace("{1}", newDate);
				tbodyHtml += rewardHtml.replace("{0}", rewardIdx).replace("{1}", rewardIdx);
				tbodyHtml += "</td>";
				rewardIdx++;
			}
			if (i == 0) {
				theadHtml += "</tr>";
			}
			tbodyHtml += "</tr>";
		}

		$("#theadRewardList").html(theadHtml);
		$("#tbodyRewardList").html(tbodyHtml);
	};

	// 제공 순위 선택에 따른 입력 값 변경.
	setRnkRewardByOfrRnk = function() {
		var value = $("select#ofrRnk").val();
		$("tr[id*=trOfrRnk]").each(function() {
			if (Number(this.id.replace("trOfrRnk", "")) <= value) {
				$(this).show();
			} else {
				$(this).hide();
			}
		});

		// 공식 테이블도 생성해준다.
		setRewardByOfrRnk();
	};

	// 달력 jquery select
	setDate = function(id) {
		var calObj = $(id);
		$(calObj).attr("readonly", true);
		makeAloneJqueryCalendar(calObj, true, true);
	};

	// 목록 체크박스 선택이벤트.
	setChkBox = function() {
		var allChk = $(".conts-checkbox");
		var val = $(this).val();
		var checked = $(this).is(":checked");

		if (val == 999) { // 전체 선택 버튼
			$(allChk).each(function() {
				if (checked) {
					$(this).prop("checked", true);
				} else {
					$(this).prop("checked", false);
				}
			});
		} else { // 한 개 선택 버튼
			var isAllChecked = true;
			var allCheckBox;
			$(allChk).each(function() {
				
				if ($(this).val() == 999) {
					allCheckBox = $(this);
				} else {
					if (!$(this).is(":checked")) {
						isAllChecked = false;
					}
				}
			});
			if (isAllChecked) {
				$(allCheckBox).prop("checked", true);
			} else {
				$(allCheckBox).prop("checked", false);
			}
		}

		// 구매형 이벤트 상품 정보 표시
		$(allChk).each(function() {
			if ($(this).val() != 999) {
				var trGood = "#trGood" + $(this).val();
				if ($(this).is(":checked")) {
					$(trGood).show();
				} else {
					$(trGood).hide();
				}
			}
		});
	};

	// 라디오 버튼 선택에 따른 활성/비활성 설정.
	setRadio = function(name, value) {
		if (name == "lstDescUseYn") {
			if (value == "1") {
				$("input#lstDesc").show();
				$("#divLstDescImage").hide();
			} else {
				$("input#lstDesc").hide();
				$("#divLstDescImage").show();
			}
		} else if (name == "contDescUseYn") {
			if (value == "1") {
				$("textarea#contDesc").show();
				$("#divContDescImage").hide();
			} else {
				$("textarea#contDesc").hide();
				$("#divContDescImage").show();
			}
		} else if (name == "eventType") {
			if (value == "21") {
				$("#divGood").hide();
				$("#divReward").show();
			} else if (value == "23") {
				$("#divGood").hide();
				$("#divReward").hide();
			} else if (value == "24") {
				$("#divGood").show();
				$("#divReward").hide();
			}
		} else if (name == "eventTypeInfo01") {
			if (value == "0") { // 즉시추첨
				$("#divRewardRnk").show();
				$("#divRewardFomula").show();
				
				// 공식 테이블도 생성해준다.
				setRewardByOfrRnk();
			} else if (value == "1") { // 사후추첨
				$("#divRewardRnk").show();
				$("#divRewardFomula").hide();
			}
		}
	};
	
	//submit.
	doSubmit = function(page, data) {
		var method = "";
		var action = "";
		var formName = "";

		if (page == "list") {
			action = "<c:url value='/event/event/list.do'/>";
			method = "get";
			formName = "#searchfrm";
			$("#searchfrm > input[id^='pageDomain.current']").val("1");
		} else if (page == "cancel") {
			action = "<c:url value='/event/event/list.do'/>";
			method = "get";
			formName = "#searchfrm";
		} else if (page == "add") {
			openOkCancelSizeDialog("add", "add_confirm", addEvent);
			return;
		} else if (page == "searchGood") {
			openGoodPop(data);
			return;
		} else if (page == "deleteGood") {
			var trGood = $("#trGood" + data);
			$(trGood).find("input[name*=sectionId]").val("");
			$(trGood).find("span[name*=sectionNm]").text("");
			$(trGood).find("input[name*=goodId]").val("");
			$(trGood).find("span[name*=goodNm]").text("");
			return;
		} 
		
		$(formName).attr({"action":action, "method":method});
		$(formName).submit();
	};
	
	//form submit
	addEvent = function() {
		openProgressDialog("작업 중", "작업 중입니다. 잠시만 기다려주세요.", null);
		common_ajxcall("POST"
			, $("form[name=frm]").serialize()
			, "<c:url value='/event/event/add.json'/>"
			, function(data, state) {
				try {
					var result = data.result;
					var errors = data.errors;
					if (result == 1) {
						//list에 있는 page이동 function을 호출한다.
						doSubmit("list");
					} else if (errors.length > 0) {
						//error 메시지를 처리합니다.
						setFormErrorMessage("#frm", data.errors);
					} else {
						openOkDialog("오류", "error", null);
					}
			    } catch(e) {
			        openOkDialog("오류", "error", null);
			    } finally {
			    	closeProgressDialog();
			    }
			}
		    , null);
	};

	var bnrImgWidth = 580; //첨부이미지 너비
	var bnrImgHeight = 86; //첨부이미지 높이
	var bnrImgRate = 1; // 비율(previewImgWidth / imgWidth)
	var bnrImgChangeFlagName = "bnrUrlImgChangeFlag";
	var previewBnrImgWidth = 500; //미리보기이미지 너비
	var previewBnrImgHeight = 78; //미리보기이미지 높이
	
	var lstImgImgWidth = 844; //첨부이미지 너비
	var lstImgImgHeight = 87; //첨부이미지 높이
	var lstImgImgRate = 1; // 비율(previewImgWidth / imgWidth)
	var lstImgImgChangeFlagName = "lstImgUrlImgChangeFlag";
	var previewLstImgImgWidth = 500; //미리보기이미지 너비
	var previewLstImgImgHeight = 55; //미리보기이미지 높이

	var contImgWidth = 632; //첨부이미지 너비
	var contImgHeight = 298; //첨부이미지 높이
	var contImgRate = 1; // 비율(previewImgWidth / imgWidth)
	var contImgChangeFlagName = "contUrlImgChangeFlag";
	var previewContImgWidth = 500; //미리보기이미지 너비
	var previewContImgHeight = 239; //미리보기이미지 높이

	var imgSize = 1000000; //파일용량
	makeImageTag = function(divId, imgId, imgName, imgUrl, imgChangeFlagName) {
		
		var editFlag = true;
		var align = "center";
		var previewBox = "<div class='preview_box'><div class='preview_img'>{img}</div></div>";

		// image tag를 생성합니다.
		var imgTag = setImgTagForEdit(editFlag, imgId, imgName, imgUrl, imgChangeFlagName, align);
		var imgBoxTag = previewBox.replace("{img}", imgTag);
		$(divId).html("");
		$(divId).append(imgBoxTag);

		//이미지 비율 setting 및 td 의 width 설정.
		var imgWidth, imgHeight, imgRate, previewImgWidth, previewImgHeight;
		if (imgId == "bnrImgRealFileRte") {
			imgWidth = bnrImgWidth;
			imgHeight = bnrImgHeight;
			previewImgWidth = previewBnrImgWidth;
			previewImgHeight = previewBnrImgHeight;
			imgRate = bnrImgRate = getPreviewImgResizeRate(bnrImgWidth, previewBnrImgWidth, true);
		} else if (imgId == "lstImgFileRte") {
			imgWidth = lstImgImgWidth;
			imgHeight = lstImgImgHeight;
			previewImgWidth = previewLstImgImgWidth;
			previewImgHeight = previewLstImgImgHeight;
			imgRate = lstImgImgRate = getPreviewImgResizeRate(lstImgImgWidth, previewLstImgImgWidth, true);
		} else if (imgId == "contImgFileRte") {
			imgWidth = contImgWidth;
			imgHeight = contImgHeight;
			previewImgWidth = previewContImgWidth;
			previewImgHeight = previewContImgHeight;
			imgRate = contImgRate = getPreviewImgResizeRate(contImgWidth, previewContImgWidth, true);
		}

		// set image
		setImg(imgId, 
				"<c:url value='/tmpDownloadAttachFile'/>?filePath=",
				imgUrl,
				imgWidth,
				imgHeight,
				editFlag,
				imgRate);

		// set size
		$(divId + " .img-frame-" + align)
			.css("min-width", previewImgWidth + "px")
			.css("width", previewImgWidth + "px")
			.css("min-height", previewImgHeight + "px")
			.css("height", previewImgHeight + "px");

		if (typeof afterSetNewImg != "undefined") {
			afterSetNewImg(divId, previewImgWidth);
		}
	}

	//이미지 등록 화면에서 호출 후 삭제버튼 이미지 위치조절.
	afterSetNewImg = function(divId, imgWidth) {
		setDelIconPosition("#divBnrDisplay", previewBnrImgWidth);
		setDelIconPosition("#divLstDescImage", previewLstImgImgWidth);
		setDelIconPosition("#divContDescImage", previewContImgWidth);
	};

	// 삭제 이미지 위치 조정.
	setDelIconPosition = function(divId, imgWidth) {
		$(divId + " div.previewImg-delDiv").css("top", -20);
		$(divId + " div.previewImg-delDiv").css("left", imgWidth + 5);
		$(divId + " div.previewImg-delDiv").css("width", 20);
	}

	//이미지 업로드. 
	imgUpload = function(imgId) {
		var imgUploadUrl = "<c:url value='/common/popup/imgUploadPop.do'/>";
		var imgWidth, imgHeight, imgRate;
		if (imgId == "bnrImgRealFileRte") {
			imgWidth = bnrImgWidth;
			imgHeight = bnrImgHeight;
			imgRate = bnrImgRate;
		} else if (imgId == "lstImgFileRte") {
			imgWidth = lstImgImgWidth;
			imgHeight = lstImgImgHeight;
			imgRate = lstImgImgRate;
		} else if (imgId == "contImgFileRte") {
			imgWidth = contImgWidth;
			imgHeight = contImgHeight;
			imgRate = contImgRate;
		} 
		
		imgUploadPop(imgUploadUrl, imgId, imgWidth, imgHeight, imgSize, imgRate);
	};

	// 이미지 삭제. 
	imgDelete = function (imgId) {
		var imgWidth, imgHeight, imgRate;
		if (imgId == "bnrImgRealFileRte") {
			imgWidth = bnrImgWidth;
			imgHeight = bnrImgHeight;
			imgRate = bnrImgRate;
		} else if (imgId == "lstImgFileRte") {
			imgWidth = lstImgImgWidth;
			imgHeight = lstImgImgHeight;
			imgRate = lstImgImgRate;
		} else if (imgId == "contImgFileRte") {
			imgWidth = contImgWidth;
			imgHeight = contImgHeight;
			imgRate = contImgRate;
		}
	 	delImg(imgId, imgWidth, imgHeight, imgRate);
	};

	setGood = function (goodObj) {
		var trGood = $("#trGood" + goodObj.plfCd);
		$(trGood).find("input[name*=sectionId]").val(goodObj.sectionId);
		$(trGood).find("span[name*=sectionNm]").text(goodObj.sectionNm);
		$(trGood).find("input[name*=goodId]").val(goodObj.goodId);
		$(trGood).find("span[name*=goodNm]").text(goodObj.goodNm);
	};
	
</script>
</head>
<body>
	<!-- Navigation Bar -->
	<div id="naviBar">
		<table class="naviTable line-default">
			<tr>
				<td>
					<div class="navigation">이벤트 &gt; 이벤트 등록</div>
				</td>
			</tr>
		</table>
	</div>
	<!-- // Navigation Bar -->
	<form:form id="searchfrm" name="searchfrm" modelAttribute="command" method="get">
		<form:hidden path="pageDomain.current" />
	</form:form>

	<form:form id="frm" name="frm" modelAttribute="command"	method="post">
		<div id="contents">
			<table class="titleNSearch line-default">
				<tr>
					<td class="contsTitle" valign="top">이벤트 등록</td>
				</tr>
			</table>

			<!-- Notice Area -->
			<table class="titleMini line-default">
			<tr>
				<td class="subTitle">이벤트 기본 정보</td>
			</tr>
			</table>
			<table class="inputTable line-default">
				<colgroup>
					<col style="width: 20%; min-width: 200px;">
					<col style="width: 80%; min-width: 200px;">
				</colgroup>
				<tr>
					<td class="firstCol title">플랫폼</td>
					<td class="lastCol">
						<input type="checkbox" id="arrPlfCd" class="conts-checkbox" value="999"/>All
						</br>
						<form:checkboxes items="${plfList}" path="arrPlfCd" itemLabel="plfNm" itemValue="plfCd" cssClass="conts-checkbox" />
						<span class="form_error" id="arrPlfCd.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title">제목</td>
					<td class="lastCol">
						<form:input path="eventTitle" cssClass="input-xxlarge"/><br/> 
						<span class="form_error" id="eventTitle.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title">이벤트 타입</td>
					<td class="lastCol">
						<form:radiobuttons path="eventType" items="${eventTypeList}" cssClass="conts-radio" /> 
						<span class="form_error" id="eventType.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title">기간</td>
					<td class="lastCol">
						<form:input cssClass="_input_calendar" path="eventStartDate" /> 
						<form:select path="eventStartHour" items="${hourList}" cssClass="conts-selectbox" />시 &nbsp;~&nbsp;
						<form:input cssClass="_input_calendar" path="eventEndDate" /> 
						<form:select path="eventEndHour" items="${hourList}" cssClass="conts-selectbox" />시 
					</td>
				</tr>
				<tr>
					<td class="firstCol title" rowspan="3">롱배너</td>
					<td class="lastCol">
						<form:radiobuttons path="bnrDisplayYn" items="${useYnList }" cssClass="conts-radio" />
					</td>
				</tr>
				<tr>
					<td class=preview-borderRight>
						<div id="divBnrDisplay"></div>
					</td>
				</tr>
				<tr>
					<td class=preview-borderRight>
						<span class="form_error" id="bnrDisplayYn.errors"></span>
						<span class="form_error" id="bnrImgRealFileRte.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title" rowspan="3">리스트 제목</td>
					<td class="lastCol">
						<form:radiobuttons path="lstDescUseYn" items="${eventTextImageUseTypeList }" cssClass="conts-radio" />
					</td>
				</tr>
				<tr>
					<td class="preview-borderRight">
						<div id="divLstDescText">
							<form:input path="lstDesc" cssClass="input-xxlarge"/><br/> 
						</div>
						<div id="divLstDescImage">
						</div>
					</td>
				</tr>
				<tr>
					<td class=preview-borderRight>
						<span class="form_error" id="lstDesc.errors"></span>
						<span class="form_error" id="lstImgFileRte.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title" rowspan="3">내용</td>
					<td class="lastCol">
						<form:radiobuttons path="contDescUseYn" items="${eventTextImageUseTypeList }" cssClass="conts-radio" />
					</td>
				</tr>
				<tr>
					<td class="preview-borderRight">
						<div id="divContDescText">
							<form:textarea path="contDesc" cssClass="input-contents" style="width:90%" rows="10" />
						</div>
						<div id="divContDescImage">
						</div>
					</td>
				</tr>
				<tr>
					<td class=preview-borderRight>
						<span class="form_error" id="contDesc.errors"></span>
						<span class="form_error" id="contImgFileRte.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title">노출여부</td>
					<td class="lastCol">
						<form:radiobuttons path="displayYn" items="${useYnList }" cssClass="conts-radio" /><br/> 
						<span class="form_error" id="displayYn.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="line" colspan="2"></td>
				</tr>
			</table>
			<!-- // Notice Area -->
			
			
			<div id="divGood" style="display: none;">
			<table class="titleMini line-default">
			<tr>
				<td class="subTitle">구매형 이벤트 정보</td>
			</tr>
			</table>
			<span class="form_error" id="goodList.errors" style="font-size: 12px;float: right;"></span>
			<table id="contsTable" class="contsTable line-default">
			<colgroup>
				<col style="width: 20%; min-width: 200px;" />
				<col style="width: 160px; min-width: 160px;" />
				<col />
				<col style="width: 160px; min-width: 160px;" />
				<col />
				<col style="width: 150px; min-width: 150px;" />
			</colgroup>
			<thead>
			<tr class="headTr">
				<td class="firstCol">플랫폼</td>
				<td class="middleCol">매장코드</td>
				<td class="middleCol">매장명</td>
				<td class="middleCol">상품코드</td>
				<td class="middleCol">상품명</td>
				<td class="lastCol">기능</td>
			</tr>
			<tbody id="tbodyGoodList">
				<c:forEach var="plfList" items="${plfList}" varStatus="status">
					<tr id="trGood${plfList.plfCd}" class="contsTr center" style="display: none;">
						<input type="hidden" name="goodList[${status.index}].plfCd" value="${plfList.plfCd}"/>
						<td class="firstCol">${plfList.plfNm}</td>
						<td class="middleCol">
							<input name="goodList[${status.index}].sectionId" class="input-large"/>
						</td>
						<td class="middleCol">
							<span name="goodList[${status.index}].sectionNm" ></span>
						</td>
						<td class="middleCol">
							<input name="goodList[${status.index}].goodId" class="input-large"/>
						</td>
						<td class="middleCol">
							<span name="goodList[${status.index}].goodNm" ></span>
						</td>
						<td class="lastCol">									
							<input type="button" class="conts-button" value="검색" title="검색" onclick="doSubmit('searchGood', '${plfList.plfCd}')" /> 
							<input type="button" class="conts-button" value="삭제" title="삭제" onclick="doSubmit('deleteGood', '${plfList.plfCd}')" /> 
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tr> 
				<td class="line" colspan="6"></td>
			</tr>
			</table>
			</div>
			
			<div id="divReward" style="display: none;">
			<table class="titleMini line-default">
			<tr>
				<td class="subTitle">참여형 이벤트 정보</td>
			</tr>
			</table>
			<table class="inputTable line-default">
				<colgroup>
					<col style="width: 20%; min-width: 200px;">
					<col style="width: 80%; min-width: 200px;">
				</colgroup>
				<tr>
					<td class="firstCol title">추첨방식</td>
					<td class="lastCol">
						<form:radiobuttons path="eventTypeInfo01" items="${eventDrawTypeList}" cssClass="conts-radio" /> 
						<span class="form_error" id="eventTypeInfo01.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title">보상타입</td>
					<td class="lastCol">
						<form:radiobuttons path="rewardType" items="${eventRewardTypeList}" cssClass="conts-radio" /> 
						<span class="form_error" id="rewardType.errors"></span>
					</td>
				</tr>
				<tr>
					<td class="firstCol title">제공순위</td>
					<td class="lastCol">
						<form:select path="ofrRnk" items="${eventOfrRnkList}" cssClass="conts-selectbox" /> 
					</td>
				</tr>
				<tr>
					<td class="line" colspan="2"></td>
				</tr>
			</table>
			
			<div id="divRewardRnk" style="display: none;">
			<span class="form_error" id="eventRnkRewardList.errors" style="font-size: 12px;float: right;"></span>
			<table id="contsTable" class="contsTable line-default">
			<colgroup>
				<col style="width: 20%; min-width: 200px;" />
				<col />
			</colgroup>
			<thead>
			<tr class="headTr">
				<td class="firstCol">등수</td>
				<td class="lastCol">상세정보</td>
			</tr>
			<tbody id="tbodyOfrRnkList">
				<c:forEach var="eventOfrRnkList" items="${eventOfrRnkList}" varStatus="status">
					<tr id="trOfrRnk${eventOfrRnkList.value}" class="contsTr center">
						<td class="firstCol">${eventOfrRnkList.value}</td>
						<td class="lastCol">
							<input type="hidden" name="eventRnkRewardList[${status.index}].ofrRnk" value="${eventOfrRnkList.value}" />
							<input name="eventRnkRewardList[${status.index}].eventRewardInfo" style="width: 95%;float: left;"/> 
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tr> 
				<td class="line" colspan="2"></td>
			</tr>
			</table>
			</div>
			
			<div id="divRewardFomula" style="display: none;">
			<span class="form_error" id="eventRewardList.errors" style="font-size: 12px;float: right;"></span>
			<table id="contsTable" class="contsTable line-default">
			<thead id="theadRewardList">
			<tr class="headTr">
			</tr>
			<tbody id="tbodyRewardList">
			</tbody>
			<tr> 
				<td class="line" colspan="6"></td>
			</tr>
			</table>
			</div>
			</div>

			<!-- Search & Button -->
			<table class="buttonTable line-default">
				<tr>
					<td class="center">
						<input type="button" id="addBtn" class="conts-button" value="저장" /> &nbsp; 
						<input type="button" id="cancelBtn" class="conts-button" value="취소" />
					</td>
				</tr>

			</table>
		</div>
	</form:form>
	
	<%@ include file="../../common/popup/goodPop.jsp"%>
</body>
</html>