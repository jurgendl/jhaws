package org.tools.hqlbuilder.webservice.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserPage extends WebPage {

	public UserPage(PageParameters parameters) {
		super(parameters);

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			String username = ((User) principal).getUsername();
			add(new Label("username", username));
		} else {
			add(new Label("username", "error"));
		}

		add(new Link<Void>("logoutLink") {

			@Override
			public void onClick() {
				getSession().invalidate();
			}

		});

	}

}