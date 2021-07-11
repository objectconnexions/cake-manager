package com.waracle.cakemgr.ui;

import com.waracle.cakemgr.CakeEntity;
import com.waracle.cakemgr.model.Model;
import com.waracle.cakemgr.model.ModelField;

public class ValueHandler {

	public void handle(HtmlNode cell, Model model, ModelField field, Object entryData) {
		if (model == Model.getModel(CakeEntity.class)) {
			if (field.getJsonName().equals("image")) {
				cell.addNode("img", null).addAttribute("src", String.valueOf(entryData));
				return;
			}
		}
		cell.addContent(String.valueOf(entryData));
		
	}

}
