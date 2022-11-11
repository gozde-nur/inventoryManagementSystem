package com.gozdenurdogan.inventorymanagementsystem.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.RoleEntity;
import com.gozdenurdogan.inventorymanagementsystem.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;




public class AuthenticatedUser extends org.springframework.security.core.userdetails.User
{

	private static final long serialVersionUID = 1L;
	private UserEntity user;
	
	public AuthenticatedUser(UserEntity user)
	{
		super(user.getEmail(), user.getPassword(), getAuthorities(user));
		this.user = user;
	}
	
	public UserEntity getUser()
	{
		return user;
	}
	
	private static Collection<? extends GrantedAuthority> getAuthorities(UserEntity user)
	{
		Set<String> roleAndPermissions = new HashSet<>();
		List<RoleEntity> roles = user.getRoles();
		
		for (RoleEntity role : roles)
		{
			roleAndPermissions.add(role.getName());
		}
		String[] roleNames = new String[roleAndPermissions.size()];
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleAndPermissions.toArray(roleNames));
		return authorities;
	}
}