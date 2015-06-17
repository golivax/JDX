package br.usp.ime.jdx.processor.extractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import br.usp.ime.jdx.entity.system.Method;
import br.usp.ime.jdx.entity.system.Type;
import br.usp.ime.jdx.filter.StringMatcher;

public class BindingResolver {

	public static Type resolveTypeBinding(StringMatcher classFilter, 
			Cache cache, ITypeBinding iTypeBinding){
		
		Type type = null;
		
		String qualifiedTypeName = 
				ExtractorUtils.getQualifiedTypeName(iTypeBinding);
		
		if (qualifiedTypeName != null && 
			!qualifiedTypeName.endsWith("[]") &&
			!classFilter.matches(qualifiedTypeName)){

			type = cache.getType(qualifiedTypeName);
		}
		
		return type;
	}
	
	public static Method resolveMethodBinding(StringMatcher classFilter, 
			Cache cache, IMethodBinding binding){
		
		Method method = null;
		
		//It is null when a generic class/interface takes a certain 
		//type parameter that does not exist in source code
		//e.g. "ArrayList<Player> opponents = new ArrayList<Player>();" and
		//the Player class/interface does not exist in source code
		if(binding.getDeclaringClass() != null){			
			
			if (!binding.getDeclaringClass().isAnonymous() && 
				!binding.getDeclaringClass().isLocal()){		
				
				String typeName = ExtractorUtils.getQualifiedTypeName(
 						binding.getDeclaringClass());
				
				if (!classFilter.matches(typeName)){
					
					String methodName = binding.getName();
					if(binding.getDeclaringClass().isParameterizedType()){
						methodName = StringUtils.substringBefore(methodName, "<");
					}
					
					List<String> parameterTypes = new ArrayList<>();
										
					for(ITypeBinding typeBinding : binding.getParameterTypes()){
						String parameterType = typeBinding.getName();
						parameterTypes.add(parameterType);
					}
										
					Type type =	cache.getType(typeName);
					
					//FIXME: Check why this is happening in JHotDraw
					if(type != null){					
						method = type.getMethod(methodName, parameterTypes);
					}
				}
			}
		}
		
		return method;
	}
	
}
