package sn.ouznoreyni.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ouznoreyni.domain.Professeur;
import sn.ouznoreyni.repository.ProfesseurRepository;
import sn.ouznoreyni.service.ProfesseurService;
import sn.ouznoreyni.service.dto.ProfesseurDTO;
import sn.ouznoreyni.service.mapper.ProfesseurMapper;

/**
 * Service Implementation for managing {@link sn.ouznoreyni.domain.Professeur}.
 */
@Service
@Transactional
public class ProfesseurServiceImpl implements ProfesseurService {

    private final Logger log = LoggerFactory.getLogger(ProfesseurServiceImpl.class);

    private final ProfesseurRepository professeurRepository;

    private final ProfesseurMapper professeurMapper;

    public ProfesseurServiceImpl(ProfesseurRepository professeurRepository, ProfesseurMapper professeurMapper) {
        this.professeurRepository = professeurRepository;
        this.professeurMapper = professeurMapper;
    }

    @Override
    public ProfesseurDTO save(ProfesseurDTO professeurDTO) {
        log.debug("Request to save Professeur : {}", professeurDTO);
        Professeur professeur = professeurMapper.toEntity(professeurDTO);
        professeur = professeurRepository.save(professeur);
        return professeurMapper.toDto(professeur);
    }

    @Override
    public ProfesseurDTO update(ProfesseurDTO professeurDTO) {
        log.debug("Request to update Professeur : {}", professeurDTO);
        Professeur professeur = professeurMapper.toEntity(professeurDTO);
        professeur = professeurRepository.save(professeur);
        return professeurMapper.toDto(professeur);
    }

    @Override
    public Optional<ProfesseurDTO> partialUpdate(ProfesseurDTO professeurDTO) {
        log.debug("Request to partially update Professeur : {}", professeurDTO);

        return professeurRepository
            .findById(professeurDTO.getId())
            .map(existingProfesseur -> {
                professeurMapper.partialUpdate(existingProfesseur, professeurDTO);

                return existingProfesseur;
            })
            .map(professeurRepository::save)
            .map(professeurMapper::toDto);
    }

    public Page<ProfesseurDTO> findAllWithEagerRelationships(Pageable pageable) {
        return professeurRepository.findAllWithEagerRelationships(pageable).map(professeurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfesseurDTO> findOne(Long id) {
        log.debug("Request to get Professeur : {}", id);
        return professeurRepository.findOneWithEagerRelationships(id).map(professeurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Professeur : {}", id);
        professeurRepository.deleteById(id);
    }
}
