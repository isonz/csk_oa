package cn.ptp.oa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ptp.oa.dao.WXMenuRepository;
import cn.ptp.oa.domain.WXMenu;
import cn.ptp.oa.service.WXMenuService;
import cn.ptp.wx.pojo.Button;
import cn.ptp.wx.pojo.ClickButton;
import cn.ptp.wx.pojo.LocationSelectButton;
import cn.ptp.wx.pojo.Menu;
import cn.ptp.wx.pojo.ParentButton;
import cn.ptp.wx.pojo.PicPhotoOrAlbumButton;
import cn.ptp.wx.pojo.PicSysphotoButton;
import cn.ptp.wx.pojo.PicWeixinButton;
import cn.ptp.wx.pojo.ScancodePushButton;
import cn.ptp.wx.pojo.ScancodeWaitmsgButton;
import cn.ptp.wx.pojo.ViewButton;

@Service
@Transactional
public class WXMenuServiceImpl implements WXMenuService {
	@Resource private WXMenuRepository dao; 


	@Override
	public Menu createMenu() {
		List<WXMenu> list = dao.findOrderByOrderNo();
		Menu menu = new Menu();
		List<Button> listBtn = new ArrayList<Button>();
		Map<Integer,ParentButton> parentMap = new HashMap<Integer,ParentButton>();
		
		for(WXMenu e:list){
			Integer parentId = e.getParentId();
			String type = e.getType();
			if(parentId==null||parentId<=0){
				if(type!=null && !type.equals("")){
					Button bt = builderButton(e);
					listBtn.add(bt);
				}
				else{
					ParentButton pb = new ParentButton();
					pb.setName(e.getName());
					parentMap.put(e.getId(), pb);
					listBtn.add(pb);
				}
			}
			else{
				ParentButton pb = parentMap.get(parentId);
				Button bt = builderButton(e);
				pb.addSubButton(bt);
			}
			Button[] btns = listBtn.toArray(new Button[listBtn.size()]);
			menu.setButton(btns);
		}
		
		return menu;
	}

	private Button builderButton(WXMenu e) {
		String type = e.getType(); 
		String name = e.getName(); 
		String key = e.getKey();
		if("click".equalsIgnoreCase(type)){
			ClickButton cb = new ClickButton();
			cb.setName(name);
			cb.setKey(key);
//			pb.addSubButton(cb);
			return cb;
		}
		else if("view".equalsIgnoreCase(type)){
			ViewButton vb = new ViewButton();
			vb.setName(name);
			vb.setUrl(e.getUrl());
//			pb.addSubButton(vb);
			return vb;
		}
		else if("scancode_waitmsg".equalsIgnoreCase(type)){
			ScancodeWaitmsgButton swb = new ScancodeWaitmsgButton();
			swb.setKey(key);
			swb.setName(name);
//			pb.addSubButton(swb);
			return swb;
		}
		else if("scancode_push".equalsIgnoreCase(type)){
			ScancodePushButton spb = new ScancodePushButton();
			spb.setKey(key);
			spb.setName(name);
//			pb.addSubButton(spb);
			return spb;
		}
		else if("pic_sysphoto".equalsIgnoreCase(type)){
			PicSysphotoButton psb = new PicSysphotoButton();
			psb.setKey(key);
			psb.setName(name);
//			pb.addSubButton(psb);
			return psb;
		}
		else if("pic_photo_or_album".equalsIgnoreCase(type)){
			PicPhotoOrAlbumButton ppab = new PicPhotoOrAlbumButton();
			ppab.setKey(key);
			ppab.setName(name);
//			pb.addSubButton(ppab);
		}
		else if("pic_weixin".equalsIgnoreCase(type)){
			PicWeixinButton pwb = new PicWeixinButton();
			pwb.setKey(key);
			pwb.setName(name);
//			pb.addSubButton(pwb);
			return pwb;
		}
		else if("location_select".equalsIgnoreCase(type)){
			LocationSelectButton lsb = new LocationSelectButton();
			lsb.setKey(key);
			lsb.setName(name);
//			pb.addSubButton(lsb);
			return lsb;
		}
		return null;
	}

}
