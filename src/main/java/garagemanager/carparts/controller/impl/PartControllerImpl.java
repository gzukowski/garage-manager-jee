package garagemanager.carparts.controller.impl;

import garagemanager.carparts.controller.api.PartController;
import garagemanager.carparts.dto.request.PatchPartRequest;
import garagemanager.carparts.dto.request.PutPartRequest;
import garagemanager.carparts.dto.response.GetPartResponse;
import garagemanager.carparts.dto.response.GetPartsResponse;
import garagemanager.carparts.service.PartService;
import garagemanager.component.DtoFunctionFactory;
import garagemanager.controller.servlet.exception.BadRequestException;
import garagemanager.controller.servlet.exception.NotFoundException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@RequestScoped
public class PartControllerImpl implements PartController {
    private final PartService service;

    private final DtoFunctionFactory factory;

    @Inject
    public PartControllerImpl(PartService service, DtoFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @Override
    public GetPartsResponse getParts() {
        return factory.partsToResponse().apply(service.findAll());
    }

    @Override
    public GetPartsResponse getCarParts(UUID id) {
        return service.findAllByCar(id)
                .map(factory.partsToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPartsResponse getUserParts(UUID id) {
        return service.findAllByUser(id)
                .map(factory.partsToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetPartResponse getPart(UUID uuid) {
        return service.find(uuid)
                .map(factory.partToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putPart(UUID id, PutPartRequest request) {
        try {
            service.create(factory.requestToPart().apply(id, request));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void patchPart(UUID id, PatchPartRequest request) {
        service.find(id).ifPresentOrElse(
                entity -> service.update(factory.updatePart().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deletePart(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.delete(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

}
