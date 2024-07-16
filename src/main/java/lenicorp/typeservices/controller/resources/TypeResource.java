package lenicorp.typeservices.controller.resources;

import jakarta.validation.Valid;
import lenicorp.typeservices.controller.repositories.TypeRepo;
import lenicorp.typeservices.controller.services.ITypeService;
import lenicorp.typeservices.model.dtos.*;
import lenicorp.typeservices.model.entities.Type;
import lenicorp.typeservices.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Profile({"dev", "prod"})
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/types") @Validated
public class TypeResource
{
    private final TypeRepo typeRepo;
    private final ITypeService typeService;

    @GetMapping(path = "") //@PreAuthorize("hasAuthority('DEV')")
    public List<ReadTypeDTO> getAllTypes()
    {
        return typeRepo.findAllTypes();
    }

    /*@GetMapping(path = "/find-by-unique-code/{uniqueCode}") //@PreAuthorize("hasAuthority('DEV')")
    public ReadTypeDTO getTypeByUniqueCode(@PathVariable String uniqueCode)
    {
        return typeRepo.findByUniqueCode(uniqueCode);
    }*/

    //@PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{group-code}")
    public List<ReadTypeDTO> getByGroupCode(@PathVariable String groupCode)
    {
        return typeRepo.findByTypeGroup(TypeGroup.valueOf(groupCode)); //typeRepo.findByGroupCode(groupCode);
    }

    //@PreAuthorize("isAnonymous()")
    @GetMapping(path = "/sous-type-of/{parentCode}")
    public List<ReadTypeDTO> getSousTypeOf(@PathVariable String parentCode)
    {
        return typeRepo.findSousTypeOf(parentCode);
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(path = "/is-sous-type-of/{parentCode}/{childCode}")
    public boolean isSousTypeOf(@PathVariable String parentCode, @PathVariable String childCode)
    {
        return typeRepo.isSousTypeOf(parentCode, childCode);
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(path = "/is-deletable/{uniqueCode}")
    public boolean isDeletable(@PathVariable String uniqueCode)
    {
        return typeRepo.isDeletable(uniqueCode);
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(path = "/exists-by-uniqueCode/{uniqueCode}")
    public boolean existsByUniqueCode(@PathVariable String uniqueCode, @RequestParam(defaultValue = "", required = false) String oldUniqueCode)
    {
        return typeService.existsByUniqueCode(uniqueCode, oldUniqueCode);
    }

    @GetMapping(path = "/exists-by-groupe-and-uniqueCode/{groupCode}/{uniqueCode}")
    public boolean existsByGroupAndUniqueCode(@PathVariable String groupCode, @PathVariable String uniqueCode)
    {
        if(TypeGroup.valueOf(groupCode) == null) return false;
        return typeRepo.existsByGroupAndUniqueCode(TypeGroup.valueOf(groupCode), uniqueCode);
    }

   // @PreAuthorize("isAnonymous()")
    @GetMapping(path = "/exists-by-name")
    public boolean existsByTypeCode(@RequestParam(defaultValue = "") String name, @RequestParam(required = false) String uniqueCode)
    {
        return typeService.existsByName(name, uniqueCode);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PostMapping(path = "/create") @ResponseStatus(HttpStatus.CREATED)
    public Type createType(@RequestBody @Valid CreateTypeDTO dto) throws UnknownHostException {

        return typeService.createType(dto);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/update") @ResponseStatus(HttpStatus.OK)
    public Type updateType(@RequestBody @Valid UpdateTypeDTO dto) throws UnknownHostException {
        return typeService.updateType(dto);
    }

    @PostMapping(path = "/add-sub-type")
    public void addSubType(TypeParamDTO dto) throws UnknownHostException {
        typeService.addSousType(dto);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/set-sous-type") @ResponseStatus(HttpStatus.OK)
    public void setSousType(@RequestBody @Valid TypeParamDTO dto) throws UnknownHostException {
        typeService.addSousType(dto);
    }

    //@PreAuthorize("hasAuthority('DEV')")
    @PutMapping(path = "/remove-sous-type") @ResponseStatus(HttpStatus.OK)
    public void removeSousType(@RequestBody @Valid TypeParamDTO dto) throws UnknownHostException {
        typeService.removeSousType(dto);
    }

    @GetMapping(path = "/search")
    public Page<ReadTypeDTO> searchTypes(@RequestParam(defaultValue = "") String key, @RequestParam(required = false)ArrayList<String> typeGroups, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return typeService.searchPageOfTypes(key, typeGroups, page, size);
    }

    @GetMapping(path = "/type-groups")
    public List<SelectOption> getTypeGroupOptions(){
        return typeService.getTypeGroupOptions();
    }

    @GetMapping(path = "/type-group-is-valid/{typeGroup}")
    public boolean typeGroupIsValid(@PathVariable String typeGroup)
    {
        return typeService.typeGroupIsValid(typeGroup);
    }

    @GetMapping(path = "/frequences/all-options")
    public List<SelectOption> getFrequenceOptions()
    {
        return typeService.getOptions(TypeGroup.TYPE_FREQUENCE);
    }

    @GetMapping(path = "/modes-prelevement/all-options")
    public List<SelectOption> getModePrelevementOptions()
    {
        return typeService.getOptions(TypeGroup.MODE_PRELEVEMENT);
    }
}
