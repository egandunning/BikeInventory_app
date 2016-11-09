class Bicycle < ApplicationRecord
	has_many :rentals
	validates :serial, presence: true
end
