package com.example.thejavatest.study;

import com.example.thejavatest.domain.Member;
import com.example.thejavatest.domain.Study;
import com.example.thejavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @Test
    void createNewStudy() {

        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);


        // Mock 객체를 조작해서
        // 특정한 매개변수를 받은 경우, 특정한 값을 리턴하거나 예외를 던지도록 만들 수 있다.
        Member member = new Member();
        member.setId(1L);
        member.setEmail("suz1wkd@gmail.com");

//        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(memberService.findById(any())).thenReturn(Optional.of(member)); //ArgumentMatchers

        assertEquals("suz1wkd@gmail.com", memberService.findById(1L).get().getEmail());
        assertEquals("suz1wkd@gmail.com", memberService.findById(2L).get().getEmail());


        // Mock 객체의 행동: void 메소드는 예외를 던지지 않고 아무런 일도 발생하지 않는다.
        // Mock 객체를 조작해서, void 메소드가 특정 매개변수를 받거나 호출된 경우 예외를 발생시킬 수 있다.
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });

        memberService.validate(2L);


        // Mock 객체를 조작해서
        // 메소드가 동일한 매개변수로 여러번 호출될 때, 각기 다르게 행동하도록 조작할 수 있다.
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member)) //첫 번째 호출
                .thenThrow(new RuntimeException()) //두 번째 호출
                .thenReturn(Optional.empty()); //세 번째 호출

        Optional<Member> findMember = memberService.findById(1L);
        assertEquals("suz1wkd@gmail.com", findMember.get().getEmail());

        assertThrows(RuntimeException.class, () -> memberService.findById(2L));

        assertEquals(Optional.empty(), memberService.findById(3L));





        // TODO memberService 객체에 findById 메소드를 1L 값으로 호출하면, Optional.of(member) 객체를 리턴하도록 Stubbing
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        // TODO studyRepository 객체에 save 메소드를 study 객체로 호출하면, study 객체 그대로 리턴하도록 Stubbing
        Study study = new Study(10, "테스트");
        when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1L, study);
        assertEquals(1L, study.getOwnerId());
    }

}