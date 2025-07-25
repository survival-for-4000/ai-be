package com.ll.demo03.mock;

import com.ll.demo03.member.domain.Member;
import com.ll.demo03.member.service.port.MemberRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberRepository implements MemberRepository {

    private final Map<Long, Member> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void resetAllMembersCredit() {
        for (Map.Entry<Long, Member> entry : storage.entrySet()) {
            Member original = entry.getValue();
            Member updated = Member.builder()
                    .id(original.getId())
                    .email(original.getEmail())
                    .name(original.getName())
                    .profile(original.getProfile())
                    .credit(100000) // 예시로 초기화 값 고정
                    .role(original.getRole())
                    .provider(original.getProvider())
                    .providerId(original.getProviderId())
                    .build();
            storage.put(entry.getKey(), updated);
        }
    }

    @Override
    public void delete(Member member) {
        storage.remove(member.getId());
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return storage.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Member save(Member newMember) {
        Long id = newMember.getId();
        if (id == null) {
            id = idGenerator.getAndIncrement();
            Member saved = Member.builder()
                    .id(id)
                    .email(newMember.getEmail())
                    .name(newMember.getName())
                    .profile(newMember.getProfile())
                    .credit(newMember.getCredit())
                    .role(newMember.getRole())
                    .provider(newMember.getProvider())
                    .providerId(newMember.getProviderId())
                    .build();
            storage.put(id, saved);
            return saved;
        } else {
            storage.put(id, newMember);
            return newMember;
        }
    }
}

