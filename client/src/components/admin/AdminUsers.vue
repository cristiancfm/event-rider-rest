<template>
  <div class="row">
    <h2>All <span style="font-family: Arial Black, serif">Members</span></h2>
  </div>
  <div class="row ps-3 pe-3">
    <ul class="nav nav-tabs" id="members-nav" role="tablist">
      <li class="nav-item" role="presentation">
        <button
          class="nav-link active"
          id="unverified-members-tab"
          data-bs-toggle="tab"
          data-bs-target="#unverified-members"
          type="button"
          role="tab"
          aria-controls="unverified members"
          aria-selected="true"
        >
          Unverified
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button
          class="nav-link"
          id="suspended-members-tab"
          data-bs-toggle="tab"
          data-bs-target="#suspended-members"
          type="button"
          role="tab"
          aria-controls="rejected categories"
          aria-selected="false"
        >
          Suspended
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button
          class="nav-link"
          id="verified-members-tab"
          data-bs-toggle="tab"
          data-bs-target="#verified-members"
          type="button"
          role="tab"
          aria-controls="verified members"
          aria-selected="false"
        >
          Verified
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button
          class="nav-link"
          id="admin-members-tab"
          data-bs-toggle="tab"
          data-bs-target="#admin-members"
          type="button"
          role="tab"
          aria-controls="administrator members"
          aria-selected="false"
        >
          Administrators
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button
          class="nav-link"
          id="all-members-tab"
          data-bs-toggle="tab"
          data-bs-target="#all-members"
          type="button"
          role="tab"
          aria-controls="all members"
          aria-selected="false"
        >
          All
        </button>
      </li>
    </ul>
    <div
      class="tab-content p-0 border-start border-end border-bottom"
      id="members-nav-content"
      style="border-radius: 0 0 5px 5px"
    >
      <div
        class="tab-pane fade show active"
        id="unverified-members"
        role="tabpanel"
        aria-labelledby="unverified-members-tab"
      >
        <UsersList
          :title="''"
          :get-users="getUnverifiedUsers"
          :edit-users="true"
        />
      </div>
      <div
        class="tab-pane fade"
        id="suspended-members"
        role="tabpanel"
        aria-labelledby="suspended-members-tab"
      >
        <UsersList
          :title="''"
          :get-users="getSuspendedUsers"
          :edit-users="true"
        />
      </div>
      <div
        class="tab-pane fade"
        id="verified-members"
        role="tabpanel"
        aria-labelledby="verified-members-tab"
      >
        <UsersList
          :title="''"
          :get-users="getVerifiedUsers"
          :edit-users="true"
        />
      </div>
      <div
        class="tab-pane fade"
        id="admin-members"
        role="tabpanel"
        aria-labelledby="admin-members-tab"
      >
        <UsersList :title="''" :get-users="getAdminUsers" :edit-users="true" />
      </div>
      <div
        class="tab-pane fade"
        id="all-members"
        role="tabpanel"
        aria-labelledby="all-members-tab"
      >
        <UsersList :title="''" :get-users="getAllUsers" :edit-users="true" />
      </div>
    </div>
  </div>
</template>

<script>
import { getStore } from "@/common/store";
import UsersList from "@/components/users/UsersList";
import UserRepository from "@/repositories/UserRepository";

export default {
  name: "AdminUsers",
  components: { UsersList },
  methods: {
    async getUnverifiedUsers() {
      return await UserRepository.findUnverified();
    },
    async getSuspendedUsers() {
      return await UserRepository.findSuspended();
    },
    async getVerifiedUsers() {
      return await UserRepository.findVerified();
    },
    async getAdminUsers() {
      return await UserRepository.findAdmin();
    },
    async getAllUsers() {
      return await UserRepository.findAll();
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
};
</script>

<style scoped></style>
