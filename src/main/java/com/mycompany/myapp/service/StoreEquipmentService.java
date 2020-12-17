package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.domain.StoreEquipment;
import com.mycompany.myapp.repository.StoreEquipmentRepository;
import com.mycompany.myapp.util.AppUtil;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link StoreEquipment}.
 */
@Service
@Transactional
public class StoreEquipmentService {

    private final Logger log = LoggerFactory.getLogger(StoreEquipmentService.class);

    private final StoreEquipmentRepository storeEquipmentRepository;

    public StoreEquipmentService(StoreEquipmentRepository storeEquipmentRepository) {
        this.storeEquipmentRepository = storeEquipmentRepository;
    }

    /**
     * Save a storeEquipment.
     *
     * @param storeEquipment the entity to save.
     * @return the persisted entity.
     */
    public StoreEquipment save(StoreEquipment storeEquipment) {
        log.debug("Request to save StoreEquipment : {}", storeEquipment);
        boolean status = findByEquipmentNameAndStore(storeEquipment);
        if (status == true) {
            throw new BadRequestAlertException("Somente um equipamento com nome " + storeEquipment.getEquipmentName(), "storeEquipment", "equipmentnameinuse");
        }
        return storeEquipmentRepository.save(storeEquipment);
    }

    /**
     * Get all the storeEquipments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StoreEquipment> findAll(Pageable pageable) {
        log.debug("Request to get all StoreEquipments");
        return storeEquipmentRepository.findAll(pageable);
    }

    /**
     * Get one storeEquipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StoreEquipment> findOne(Long id) {
        log.debug("Request to get StoreEquipment : {}", id);
        return storeEquipmentRepository.findById(id);
    }

    /**
     * Delete the storeEquipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StoreEquipment : {}", id);
        storeEquipmentRepository.deleteById(id);
    }

    public List<StoreEquipment> findByStore(Long idStore) {
        log.debug("Search equipment by store");
        Store store = new Store();
        store.setId(idStore);
        return storeEquipmentRepository.findByStore(store);
    }

    private boolean findByEquipmentNameAndStore(StoreEquipment storeEquipment) {
 
        /* Realizo uma consulta passando como parametro o objeto Store, onde me retorna uma lista de 
        equipamentos. Se a lista nao tiver vazia, ela e percorrida e verifica se o nome informado 
        corresponde a algum item da lista. Aqui temos um problema: Pode ocorrer que exista no banco 
        um nome de equipamento com espacos duplicados, Exemplo: "Ar   Condicionado", isso seria 
        diferente de "Ar Condicionado". Entao, ao percorrer a lista, criei uma funcao que retira os espacos
        duplicados e so assim eu realizo a comparacao do valor que o usuario informou com o valor do banco.
        Utilizo a comparacao "equalsIgnoreCase" para que seja ignorado maiuscula e minuscula. E para nao ter 
        mais problemas futuros com strings com espacos duplicados, nos set's da entidade StoreEquipment atribui
        a funcao AppUtil.removeDuplicateSpace, assim todos os post para esta entidade ja sera retirado o espacamento.
        */
        List<StoreEquipment> list = storeEquipmentRepository.findByStore(storeEquipment.getStore());

        if (!list.isEmpty()) {
            String value;
            for (StoreEquipment e : list) {
                
                System.out.println("Valor: "+e.getEquipmentName());
              
                /* Remove todos os espacos duplicados */
                value = AppUtil.removeDuplicateSpace(e.getEquipmentName());
                if (value.equalsIgnoreCase(storeEquipment.getEquipmentName())) {
                    return true;
                }
           
            }
        }

        return false;

    }
}
