package cn.ideaswork.ideacoder.domain.pm.persona;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonaService {
  Persona savePersona(Persona persona);

  List<Persona> getAllPersonas();

  Persona getPersonaById(final String id);

  Persona updatePersonaById(Persona persona, final String id);

  void deletePersonaById(final String id);

  Boolean isPersonaExist(final String id);

  Page<Persona> getPageByCondition(PersonaDTO personaDTO, Pageable pageable);

  List<Persona> getListByCondition(PersonaDTO personaDTO);
}
