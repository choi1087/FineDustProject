package org.spring.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spring.dto.MemberVO;
import org.spring.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Inject
	private MemberService service;
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws Exception {
		logger.info("home");
		
		
		//int spotidx = service.selectSpotidx("");
		
		//model.addAttribute("selectSpotidx",spotidx);
		//System.out.print(spotidx);
		
		//service.insertMember(0,"TESTzzz",123,"120000",0);
		
		
		//System.out.println(memberList.get(0).getId());
		//System.out.println(memberList.get(0).getName());
		return "home";
	}
//	private int no;
//	private String spotName;
//	private int dustDegree;
//	private String time;
//	private int isIndoor;
	
	@RequestMapping(value= "/POST", method = {RequestMethod.POST})
	public String androidPage(HttpServletRequest request, Model model) {
		System.out.println("서버에서 안드로이드 접속 요청함");	//?time=__&data=___
		try{
				
				String spotName = request.getParameter("spotName");
				String dustDegree = request.getParameter("dustDegree");
				String time = request.getParameter("time");
				String isIndoor = request.getParameter("isIndoor");
				
				
				System.out.println("안드로이드에서 받아온 spotName : " + spotName);
				System.out.println("안드로이드에서 받아온 dustDegree : " + dustDegree);
				System.out.println("안드로이드에서 받아온 time : " + time);
				System.out.println("안드로이드에서 받아온 isIndoor : " + isIndoor);
				
				MemberVO tmp = new MemberVO(spotName,Double.parseDouble(dustDegree),time,Integer.parseInt(isIndoor));
				
				service.insertMember(tmp);
				//model.addAttribute("post",);
				
				return "post";
				
		}catch (Exception e){
				e.printStackTrace();
				return "null";
		}
	}
	

	@RequestMapping(value= "/GET/{spotName}/{isIndoor}", method = {RequestMethod.GET})
	@ResponseBody() // JSON
	public List<Map<String, Object>> dodo(@PathVariable String spotName,@PathVariable int isIndoor, Locale locale, Model model) throws Exception {
		MemberVO find = new MemberVO(spotName,isIndoor);
		List<MemberVO> memberList = service.selectMember(find);
		
		model.addAttribute("memberList", memberList );
		System.out.println(memberList.get(0).getNo());
		System.out.println(memberList.get(0).getSpotName());
		System.out.println(memberList.get(0).getDustDegree());
		System.out.println(memberList.get(0).getTime());
		System.out.println(memberList.get(0).getIsIndoor());
		System.out.println(memberList.size());
		
		List<Map<String,Object>> ListHash= new ArrayList<Map<String,Object>>();
		
		for(int i = 0; i<memberList.size(); i++)
		{
			MemberVO tmp = memberList.get(i);
			
			Map<String, Object> list = new HashMap<String, Object>();
			
			list.put("no", tmp.getNo());
			list.put("spotName", tmp.getSpotName());
			list.put("dustDegree", tmp.getDustDegree());
			list.put("time", tmp.getTime());
			list.put("isIndoor", tmp.getIsIndoor());
			ListHash.add(list);
		}
	
		
		
		return ListHash;
	}

}
