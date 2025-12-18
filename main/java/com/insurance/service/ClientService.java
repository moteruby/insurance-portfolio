package com.insurance.service;

import com.insurance.dto.ClientDTO;
import com.insurance.exception.DuplicateResourceException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.Client;
import com.insurance.model.ClientStatus;
import com.insurance.model.Policy;
import com.insurance.repository.ClientRepository;
import com.insurance.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {
    
    private final ClientRepository clientRepository;
    private final PolicyRepository policyRepository;
    
    public List<ClientDTO> getAllClients(String region, ClientStatus status) {
        List<Client> clients;
        
        if (region != null && status != null) {
            clients = clientRepository.findByRegionAndStatus(region, status);
        } else if (region != null) {
            clients = clientRepository.findByRegion(region);
        } else if (status != null) {
            clients = clientRepository.findByStatus(status);
        } else {
            clients = clientRepository.findAll();
        }
        
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        return convertToDTO(client);
    }
    
    public ClientDTO createClient(ClientDTO clientDTO) {
        // Проверка на дубликат email
        if (clientDTO.getEmail() != null && clientRepository.findByEmail(clientDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Client", "email", clientDTO.getEmail());
        }
        
        Client client = convertToEntity(clientDTO);
        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }
    
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        
        // Проверка на дубликат email (если email изменился)
        if (clientDTO.getEmail() != null && !clientDTO.getEmail().equals(client.getEmail())) {
            if (clientRepository.findByEmail(clientDTO.getEmail()).isPresent()) {
                throw new DuplicateResourceException("Client", "email", clientDTO.getEmail());
            }
        }
        
        updateClientFromDTO(client, clientDTO);
        Client updatedClient = clientRepository.save(client);
        return convertToDTO(updatedClient);
    }
    
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        clientRepository.delete(client);
    }
    
    public List<Policy> getClientPolicies(Long clientId) {
        // Проверяем существование клиента
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client", "id", clientId);
        }
        return policyRepository.findByClientId(clientId);
    }
    
    public List<ClientDTO> searchClients(String searchTerm) {
        List<Client> clients = clientRepository.searchByName(searchTerm);
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFullName(client.getFullName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        dto.setRegion(client.getRegion());
        dto.setBirthDate(client.getBirthDate());
        dto.setStatus(client.getStatus());
        dto.setRegistrationDate(client.getRegistrationDate());
        return dto;
    }
    
    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        client.setFullName(dto.getFullName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        client.setRegion(dto.getRegion());
        client.setBirthDate(dto.getBirthDate());
        client.setStatus(dto.getStatus() != null ? dto.getStatus() : ClientStatus.ACTIVE);
        return client;
    }
    
    private void updateClientFromDTO(Client client, ClientDTO dto) {
        if (dto.getFullName() != null) client.setFullName(dto.getFullName());
        if (dto.getEmail() != null) client.setEmail(dto.getEmail());
        if (dto.getPhone() != null) client.setPhone(dto.getPhone());
        if (dto.getAddress() != null) client.setAddress(dto.getAddress());
        if (dto.getRegion() != null) client.setRegion(dto.getRegion());
        if (dto.getBirthDate() != null) client.setBirthDate(dto.getBirthDate());
        if (dto.getStatus() != null) client.setStatus(dto.getStatus());
    }
}
