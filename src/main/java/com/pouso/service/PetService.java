package com.pouso.service;

import com.pouso.dto.PetOwnerListDTO;
import com.pouso.dto.PetOwnerListDTO.OwnerItem;
import com.pouso.dto.PetOwnerListDTO.PetItem;
import com.pouso.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PetOwnerListDTO listPaged(int page, int size, String sortBy, String sortDir, String q) {
        int offset = page * size;
        List<OwnerItem> owners = petRepository.listarProprietariosPaginado(offset, size, sortBy, sortDir, q);
        long total = petRepository.contarProprietarios(q);

        if (!owners.isEmpty()) {
            List<String> cpfs = owners.stream().map(OwnerItem::getCpf).collect(Collectors.toList());
            List<PetItem> allPets = petRepository.buscarPetsPorCpfs(cpfs);

            Map<String, List<PetItem>> petsByCpf = allPets.stream()
                    .collect(Collectors.groupingBy(PetItem::getCpfDono));

            for (OwnerItem owner : owners) {
                owner.setPets(petsByCpf.getOrDefault(owner.getCpf(), List.of()));
            }
        }

        return new PetOwnerListDTO(owners, page, size, total);
    }
}
