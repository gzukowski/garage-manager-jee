//package garagemanager.carparts.controller.impl;
//
//import garagemanager.carparts.controller.api.PartController;
//import garagemanager.carparts.dto.response.GetPartResponse;
//import garagemanager.carparts.dto.response.GetPartsResponse;
//import garagemanager.carparts.service.PartService;
//import garagemanager.component.DtoFunctionFactory;
//import garagemanager.controller.servlet.exception.BadRequestException;
//import garagemanager.controller.servlet.exception.NotFoundException;
//
//import java.util.UUID;
//
//public class PartControllerImpl implements PartController {
//    private final PartService service;
//
//    private final DtoFunctionFactory factory;
//
//    public PartControllerImpl(PartService service, DtoFunctionFactory factory) {
//        this.service = service;
//        this.factory = factory;
//    }
//
//    @Override
//    public GetPartsResponse getParts() {
//        return factory.partsToResponse().apply(service.findAll());
//    }
//
//    @Override
//    public GetPartsResponse getCarParts(UUID id) {
//        return service.findAllByCar(id)
//                .map(factory.partsToResponse())
//                .orElseThrow(NotFoundException::new);
//    }
//
//    @Override
//    public GetPartsResponse getUserParts(UUID id) {
//        return service.findAllByUser(id)
//                .map(factory.charactersToResponse())
//                .orElseThrow(NotFoundException::new);
//    }
//
//    @Override
//    public GetPartResponse getCharacter(UUID uuid) {
//        return service.find(uuid)
//                .map(factory.characterToResponse())
//                .orElseThrow(NotFoundException::new);
//    }
//
//    @Override
//    public void putCharacter(UUID id, PutCharacterRequest request) {
//        try {
//            service.create(factory.requestToCharacter().apply(id, request));
//        } catch (IllegalArgumentException ex) {
//            throw new BadRequestException(ex);
//        }
//    }
//
//    @Override
//    public void patchCharacter(UUID id, PatchCharacterRequest request) {
//        service.find(id).ifPresentOrElse(
//                entity -> service.update(factory.updateCharacter().apply(entity, request)),
//                () -> {
//                    throw new NotFoundException();
//                }
//        );
//    }
//
//    @Override
//    public void deleteCharacter(UUID id) {
//        service.find(id).ifPresentOrElse(
//                entity -> service.delete(id),
//                () -> {
//                    throw new NotFoundException();
//                }
//        );
//    }
//
//    @Override
//    public byte[] getCharacterPortrait(UUID id) {
//        return service.find(id)
//                .map(Character::getPortrait)
//                .orElseThrow(NotFoundException::new);
//    }
//
//    @Override
//    public void putCharacterPortrait(UUID id, InputStream portrait) {
//        service.find(id).ifPresentOrElse(
//                entity -> service.updatePortrait(id, portrait),
//                () -> {
//                    throw new NotFoundException();
//                }
//        );
//    }
//
//}
